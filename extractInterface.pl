#!/usr/bin/perl

## Just a perl script to extract an interface from a class.
## It extract all public methods and copies the javadoc.
## - With the option --lazy it does the work for Lazy classes 
##     and generates the file with the 'Lazy' prefix
## - Without --lazy it generates a file with the 'I' prefix

my ($i, $o, $lazy);

foreach (@ARGV) {
  if (/^--input=(.*)$/) {
    $o = $i = $1;
  } elsif (/^--lazy$/) {
    $lazy = 1;
  }
}

my $iclass = $1 if ($i =~ /^.*\/([^\.]+)\.java$/);
my $oclass = ($lazy ? "Lazy" : "I") . $iclass;

$o =~ s/$iclass/$oclass/;
my $c = 0;

open(F, $i) || die $!;
my ($in, $com, $ingq, $inclass, $inh, $head, $body, $inmeth, $meth, $dep) = (0, "", 0, 0, 1, "", "", 0, "", 0);
my ($a, $b) = (0,0);
while(<F>) {
   s/\r+//g;
   s/^\s+//g;
   s/^(\*.*)$/ $1/g;
   $inh = 0 if (/^\/\*\*/);
   $head .= $_ if ($inh);
   $inclass=1 if (!$in && $ingq && m/(^|\s+|\()(class|enum|new) /);
   if ($ingq && !$inclass) {
      $in = 1 if (/^\/\**\s*$/);
      $com = "" if (/^\/\**\s*$/);
      $dep = 1 if (/^\s*\@Deprecated/);
      next if /static/;
      next if /$iclass\s*\(/;
      $inmeth = 1 if (!$inmeth && !$in && /(public .*?\(.*)\s*$/);
      $dep = $inmeth = 0 if ($dep && $inmeth);
      $dep = $in = 0 if ($dep && $in);
      $meth .= $_ if ($inmeth);
      if ($inmeth && /\{/) {
         $meth =~ s/final\s+//g;
         $meth =~ s/public\s+//g;
         $meth =~ s/\{\s*//g;
         $meth =~ s/\s+$//g;
         if (!/$oclass/) {
            $meth =~ s/([^\(]*?)$iclass(\s+.*\()/$1$oclass<T>$2/g if ($lazy);
            $meth =~ s/\n/ /g;
            $meth =~ s/ +/ /g;
            $body .= "$com" if (!$in);
            $body .= "  " .$meth . ";\n\n";
         }
         $com = "";
         $meth = "";
         $inmeth = 0;
      }
      $com .= "  " . $_ if ($in);
      $in = 0 if (/^\s+\*\/\s*$/);
   }
   if ($inclass) {
      my $l = $_;
      $a ++ while($l =~ /(\{)/g);
      $b ++ while($l =~ /(\})/g);
      $inclass = $a = $b = 0 if ($a == $b);
   }
   $ingq = 1 if (!$ingq && m/(^|\s+)class /);
   #$body .= "$c $ingq $inclass $a $b\n";
   $c ++;
}
close(F);

my $class = "/**\n * $oclass.\n * \@param <T>\n */\npublic interface $oclass";
if ($lazy) {
  $class .= "<T> extends LazyBase<T>" if ($lazy);
  $head .= "import com.google.gwt.query.client.GQuery.*;\n";
  $head .= "import com.google.gwt.query.client.LazyBase;\n\n";
}

open(F, ">$o") || die $!;
print F $head . $class . "{\n\n" . $body . "}\n";
close(F);
