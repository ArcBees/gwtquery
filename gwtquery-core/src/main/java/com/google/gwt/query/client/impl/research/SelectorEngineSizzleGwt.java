/*
 * Copyright 2011, The gwtquery team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.gwt.query.client.impl.research;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.query.client.impl.SelectorEngineImpl;
import com.google.gwt.query.client.js.JsUtils;

/**
 * Pure Javascript Selector Engine Gwt Implementation based on
 * Sizzle CSS Selector Engine v1.0.
 *
 * It has so many JSNI code, the idea is to make an entire implementation
 * using Java. Right now it performs worse than pure JSNI implementation.
 *
 */
public class SelectorEngineSizzleGwt extends SelectorEngineImpl {

  public static native boolean contains(Object a, Object b) /*-{
    var ret =
      document.compareDocumentPosition ?
        (a.compareDocumentPosition(b) & 16):
        a !== b && (a.contains ? a.contains(b) : true);
    return ret ? true : false;
  }-*/;

  public static native JavaScriptObject createExpr() /*-{
    var done = 0;
    $wnd.Expr = {
      order: [ "ID", "NAME", "TAG" ],
      match: {
        ID: /#((?:[\w\u00c0-\uFFFF-]|\\.)+)/,
        CLASS: /\.((?:[\w\u00c0-\uFFFF-]|\\.)+)/,
        NAME: /\[name=['"]*((?:[\w\u00c0-\uFFFF-]|\\.)+)['"]*\]/,
        ATTR: /\[\s*((?:[\w\u00c0-\uFFFF-]|\\.)+)\s*(?:(\S?=)\s*(['"]*)(.*?)\3|)\s*\]/,
        TAG: /^((?:[\w\u00c0-\uFFFF\*-]|\\.)+)/,
        CHILD: /:(only|nth|last|first)-child(?:\((even|odd|[\dn+-]*)\))?/,
        POS: /:(nth|eq|gt|lt|first|last|even|odd)(?:\((\d*)\))?(?=[^-]|$)/,
        PSEUDO: /:((?:[\w\u00c0-\uFFFF-]|\\.)+)(?:\((['"]?)((?:\([^\)]+\)|[^\(\)]*)+)\2\))?/,
        CHUNKER: /((?:\((?:\([^()]+\)|[^()]+)+\)|\[(?:\[[^[\]]*\]|['"][^'"]*['"]|[^[\]'"]+)+\]|\\.|[^ >+~,(\[\\]+)+|[>+~])(\s*,\s*)?((?:.|\r|\n)*)/g
      },
      leftMatch: {},
      attrMap: {
        "class": "className",
        "for": "htmlFor"
      },
      attrHandle: {
        href: function(elem){
          return elem.getAttribute("href");
        }
      },
      relative: {
        "+": function(checkSet, part){
          var isPartStr = typeof part === "string",
            isTag = isPartStr && !/\W/.test(part),
            isPartStrNotTag = isPartStr && !isTag;
          if ( isTag ) {
            part = part.toLowerCase();
          }
          for ( var i = 0, l = checkSet.length, elem; i < l; i++ ) {
            if ( (elem = checkSet[i]) ) {
              while ( (elem = elem.previousSibling) && elem.nodeType !== 1 ) {}

              checkSet[i] = isPartStrNotTag || elem && elem.nodeName.toLowerCase() === part ?
                elem || false :
                elem === part;
            }
          }
          if ( isPartStrNotTag ) {
            @com.google.gwt.query.client.impl.research.SelectorEngineSizzleGwt::filter(Ljava/lang/String;Lcom/google/gwt/core/client/JsArray;ZLjava/lang/Object;)( part, checkSet, true );
          }
        },
        ">": function(checkSet, part){
          var isPartStr = typeof part === "string";
          if ( isPartStr && !/\W/.test(part) ) {
            part = part.toLowerCase();

            for ( var i = 0, l = checkSet.length; i < l; i++ ) {
              var elem = checkSet[i];
              if ( elem ) {
                var parent = elem.parentNode;
                checkSet[i] = parent.nodeName.toLowerCase() === part ? parent : false;
              }
            }
          } else {
            for ( var i = 0, l = checkSet.length; i < l; i++ ) {
              var elem = checkSet[i];
              if ( elem ) {
                checkSet[i] = isPartStr ?
                  elem.parentNode :
                  elem.parentNode === part;
              }
            }
            if ( isPartStr ) {
              @com.google.gwt.query.client.impl.research.SelectorEngineSizzleGwt::filter(Ljava/lang/String;Lcom/google/gwt/core/client/JsArray;ZLjava/lang/Object;)( part, checkSet, true );
            }
          }
        },
        "-": function(checkSet, part){
          var doneName = done++;
          if ( typeof part === "string" && !/\W/.test(part) ) {
            checkFn = $wnd.dirNodeCheck;
            @com.google.gwt.query.client.impl.research.SelectorEngineSizzleGwt::dirNodeCheck(Ljava/lang/String;Ljava/lang/Object;ILjava/lang/Object;)("parentNode", part, doneName, checkSet);
          } else {
            @com.google.gwt.query.client.impl.research.SelectorEngineSizzleGwt::dirCheck(Ljava/lang/String;Ljava/lang/Object;ILjava/lang/Object;)("parentNode", part, doneName, checkSet);
          }
        },
        "~": function(checkSet, part){
          var doneName = done++;
          if ( typeof part === "string" && !/\W/.test(part) ) {
            @com.google.gwt.query.client.impl.research.SelectorEngineSizzleGwt::dirNodeCheck(Ljava/lang/String;Ljava/lang/Object;ILjava/lang/Object;)("previousSibling", part, doneName, checkSet);
          } else {
            @com.google.gwt.query.client.impl.research.SelectorEngineSizzleGwt::dirCheck(Ljava/lang/String;Ljava/lang/Object;ILjava/lang/Object;)("previousSibling", part, doneName, checkSet);
          }
        }
      },
      find: {
        ID: function(match, context){
          if ( typeof context.getElementById !== "undefined") {
            var m = context.getElementById(match[1]);
            return m ? [m] : [];
          }
        },
        NAME: function(match, context){
          if ( typeof context.getElementsByName !== "undefined" ) {
            var ret = [], results = context.getElementsByName(match[1]);

            for ( var i = 0, l = results.length; i < l; i++ ) {
              if ( results[i].getAttribute("name") === match[1] ) {
                ret.push( results[i] );
              }
            }
            return ret.length === 0 ? null : ret;
          }
        },
        TAG: function(match, context){
          return context.getElementsByTagName(match[1]);
        }
      },
      preFilter: {
        CLASS: function(match, curLoop, inplace, result, not){
          match = " " + match[1].replace(/\\/g, "") + " ";
          for ( var i = 0, elem; (elem = curLoop[i]) != null; i++ ) {
            if ( elem ) {
              if ( not ^ (elem.className && (" " + elem.className + " ").replace(/[\t\n]/g, " ").indexOf(match) >= 0) ) {
                if ( !inplace ) {
                  result.push( elem );
                }
              } else if ( inplace ) {
                curLoop[i] = false;
              }
            }
          }
          return false;
        },
        ID: function(match){
          return match[1].replace(/\\/g, "");
        },
        TAG: function(match, curLoop){
          return match[1].toLowerCase();
        },
        CHILD: function(match){
          if ( match[1] === "nth" ) {
            // parse equations like 'even', 'odd', '5', '2n', '3n+2', '4n-1', '-n+6'
            var test = /(-?)(\d*)n((?:\+|-)?\d*)/.exec(
              match[2] === "even" && "2n" || match[2] === "odd" && "2n+1" ||
              !/\D/.test( match[2] ) && "0n+" + match[2] || match[2]);

            // calculate the numbers (first)n+(last) including if they are negative
            match[2] = (test[1] + (test[2] || 1)) - 0;
            match[3] = test[3] - 0;
          }
          match[0] = done++;
          return match;
        },
        ATTR: function(match, curLoop, inplace, result, not){
          var name = match[1].replace(/\\/g, "");
          if ($wnd.Expr.attrMap[name] ) {
            match[1] = $wnd.Expr.attrMap[name];
          }
          if ( match[2] === "~=" ) {
            match[4] = " " + match[4] + " ";
          }
          return match;
        },
        PSEUDO: function(match, curLoop, inplace, result, not){
          if ( match[1] === "not" ) {
            // If we're dealing with a complex expression, or a simple one
            if ( ( $wnd.Expr.match.CHUNKER.exec(match[3]) || "" ).length > 1 || /^\w/.test(match[3]) ) {
              match[3] = @com.google.gwt.query.client.impl.research.SelectorEngineSizzleGwt::select(Ljava/lang/String;Lcom/google/gwt/dom/client/Node;Lcom/google/gwt/core/client/JsArray;Lcom/google/gwt/core/client/JsArray;)(match[3], null, null, curLoop);
            } else {
              var ret = @com.google.gwt.query.client.impl.research.SelectorEngineSizzleGwt::filter(Ljava/lang/String;Lcom/google/gwt/core/client/JsArray;ZLjava/lang/Object;)(match[3], curLoop, inplace, true ^ not);
              if ( !inplace ) {
                result.push.apply( result, ret );
              }
              return false;
            }
          } else if ( $wnd.Expr.match.POS.test( match[0] ) || $wnd.Expr.match.CHILD.test( match[0] ) ) {
            return true;
          }
          return match;
        },
        POS: function(match){
          match.unshift( true );
          return match;
        }
      },
      filters: {
        enabled: function(elem){
          return elem.disabled === false && elem.type !== "hidden";
        },
        disabled: function(elem){
          return elem.disabled === true;
        },
        checked: function(elem){
          return elem.checked === true;
        },
        selected: function(elem){
          // Accessing this property makes selected-by-default
          // options in Safari work properly
          elem.parentNode.selectedIndex;
          return elem.selected === true;
        },
        parent: function(elem){
          return !!elem.firstChild;
        },
        empty: function(elem){
          return !elem.firstChild;
        },
        has: function(elem, i, match){
          return !!@com.google.gwt.query.client.impl.research.SelectorEngineSizzleGwt::select(Ljava/lang/String;Lcom/google/gwt/dom/client/Node;Lcom/google/gwt/core/client/JsArray;Lcom/google/gwt/core/client/JsArray;)(match[3], elem, null, null).length;
        },
        header: function(elem){
          return /h\d/i.test( elem.nodeName );
        },
        text: function(elem){
          return "text" === elem.type;
        },
        radio: function(elem){
          return "radio" === elem.type;
        },
        checkbox: function(elem){
          return "checkbox" === elem.type;
        },
        file: function(elem){
          return "file" === elem.type;
        },
        password: function(elem){
          return "password" === elem.type;
        },
        submit: function(elem){
          return "submit" === elem.type;
        },
        image: function(elem){
          return "image" === elem.type;
        },
        reset: function(elem){
          return "reset" === elem.type;
        },
        button: function(elem){
          return "button" === elem.type || elem.nodeName.toLowerCase() === "button";
        },
        input: function(elem){
          return /input|select|textarea|button/i.test(elem.nodeName);
        }
      },
      setFilters: {
        first: function(elem, i){
          return i === 0;
        },
        last: function(elem, i, match, array){
          return i === array.length - 1;
        },
        even: function(elem, i){
          return i % 2 === 0;
        },
        odd: function(elem, i){
          return i % 2 === 1;
        },
        lt: function(elem, i, match){
          return i < match[3] - 0;
        },
        gt: function(elem, i, match){
          return i > match[3] - 0;
        },
        nth: function(elem, i, match){
          return match[3] - 0 === i;
        },
        eq: function(elem, i, match){
          return match[3] - 0 === i;
        }
      },
      filter: {
        PSEUDO: function(elem, match, i, array){
          var name = match[1], filter = $wnd.Expr.filters[ name ];
          if ( filter ) {
            return filter( elem, i, match, array );
          } else if ( name === "contains" ) {
            return (elem.textContent || elem.innerText || @com.google.gwt.query.client.impl.research.SelectorEngineSizzleGwt::getText(Ljava/lang/Object;)([ elem ]) || "").indexOf(match[3]) >= 0;
          } else if ( name === "not" ) {
            var not = match[3];

            for ( var i = 0, l = not.length; i < l; i++ ) {
              if ( not[i] === elem ) {
                return false;
              }
            }
            return true;
          } else {
            @com.google.gwt.query.client.impl.research.SelectorEngineSizzleGwt::error(Ljava/lang/String;)("Syntax error, unrecognized expression: " + name);
          }
        },
        CHILD: function(elem, match){
          var type = match[1], node = elem;
          switch (type) {
            case 'only':
            case 'first':
              while ( (node = node.previousSibling) )   {
                if ( node.nodeType === 1 ) {
                  return false;
                }
              }
              if ( type === "first" ) {
                return true;
              }
              node = elem;
            case 'last':
              while ( (node = node.nextSibling) )   {
                if ( node.nodeType === 1 ) {
                  return false;
                }
              }
              return true;
            case 'nth':
              var first = match[2], last = match[3];
              if ( first === 1 && last === 0 ) {
                return true;
              }
              var doneName = match[0],
              parent = elem.parentNode;
              if ( parent && (parent.sizcache !== doneName || !elem.nodeIndex) ) {
                var count = 0;
                for ( node = parent.firstChild; node; node = node.nextSibling ) {
                  if ( node.nodeType === 1 ) {
                    node.nodeIndex = ++count;
                  }
                }
                parent.sizcache = doneName;
              }
              var diff = elem.nodeIndex - last;
              if ( first === 0 ) {
                return diff === 0;
              } else {
                return ( diff % first === 0 && diff / first >= 0 );
              }
          }
        },
        ID: function(elem, match){
          return elem.nodeType === 1 && elem.getAttribute("id") === match;
        },
        TAG: function(elem, match){
          return (match === "*" && elem.nodeType === 1) || elem.nodeName.toLowerCase() === match;
        },
        CLASS: function(elem, match){
          return (" " + (elem.className || elem.getAttribute("class")) + " ")
            .indexOf( match ) > -1;
        },
        ATTR: function(elem, match){
          var name = match[1],
            result = $wnd.Expr.attrHandle[ name ] ?
              $wnd.Expr.attrHandle[ name ]( elem ) :
              elem[ name ] != null ?
                elem[ name ] :
                elem.getAttribute( name ),
            value = result + "",
            type = match[2],
            check = match[4];
          return result == null ?
            type === "!=" :
            type === "=" ?
            value === check :
            type === "*=" ?
            value.indexOf(check) >= 0 :
            type === "~=" ?
            (" " + value + " ").indexOf(check) >= 0 :
            !check ?
            value && result !== false :
            type === "!=" ?
            value !== check :
            type === "^=" ?
            value.indexOf(check) === 0 :
            type === "$=" ?
            value.substr(value.length - check.length) === check :
            type === "|=" ?
            value === check || value.substr(0, check.length + 1) === check + "-" :
            false;
        },
        POS: function(elem, match, i, array){
          var name = match[2], filter = $wnd.Expr.setFilters[ name ];
          if ( filter ) {
            return filter( elem, i, match, array );
          }
        }
      }
    };

    for ( var type in $wnd.Expr.match ) {
      $wnd.Expr.match[ type ] = new RegExp( $wnd.Expr.match[ type ].source + /(?![^\[]*\])(?![^\(]*\))/.source );
      $wnd.Expr.leftMatch[ type ] = new RegExp( /(^(?:.|\r|\n)*?)/.source + $wnd.Expr.match[ type ].source.replace(/\\(\d+)/g, function(all, num){
        return "\\" + (num - 0 + 1);
      }));
    }

    return $wnd.Expr;
  }-*/;

  public static native void dirCheck(String dir, Object cur, int doneName, Object checkSet) /*-{
     for ( var i = 0, l = checkSet.length; i < l; i++ ) {
       var elem = checkSet[i];
       if ( elem ) {
         elem = elem[dir];
         var match = false;
         while ( elem ) {
           if ( elem.sizcache === doneName ) {
             match = checkSet[elem.sizset];
             break;
           }
           if ( elem.nodeType === 1 ) {
             elem.sizcache = doneName;
             elem.sizset = i;
             if ( typeof cur !== "string" ) {
               if ( elem === cur ) {
                 match = true;
                 break;
               }
             } else if ( @com.google.gwt.query.client.impl.research.SelectorEngineSizzleGwt::filter(Ljava/lang/String;Lcom/google/gwt/core/client/JsArray;ZLjava/lang/Object;)( cur, [elem], false ).length > 0 ) {
               match = elem;
               break;
             }
           }
           elem = elem[dir];
         }
         checkSet[i] = match;
       }
     }
   }-*/;

   public static native void dirNodeCheck(String dir, Object cur, int doneName, Object checkSet) /*-{
    for ( var i = 0, l = checkSet.length; i < l; i++ ) {
      var elem = checkSet[i];
      if ( elem ) {
        elem = elem[dir];
        var match = false;
        while ( elem ) {
          if ( elem.sizcache === doneName ) {
            match = checkSet[elem.sizset];
            break;
          }
          if ( elem.nodeType === 1){
            elem.sizcache = doneName;
            elem.sizset = i;
          }
          if ( elem.nodeName.toLowerCase() === cur ) {
            match = elem;
            break;
          }
          elem = elem[dir];
        }
        checkSet[i] = match;
      }
    }
  }-*/;

  public static void error(String msg) {
    throw new IllegalArgumentException("Syntax error, unrecognized expression: " + msg);
  }

  public static native JsArray<Node> filter(String expr, JsArray<Node> set, boolean inplace, Object not) /*-{
    var old = expr, result = [], curLoop = set, match, anyFound;
    while ( expr && set.length ) {
      for ( var type in $wnd.Expr.filter ) {
        if ( (match = $wnd.Expr.leftMatch[ type ].exec( expr )) != null && match[2] ) {
          var filter = $wnd.Expr.filter[ type ], found, item, left = match[1];
          anyFound = false;
          match.splice(1,1);
          if ( left.substr( left.length - 1 ) === "\\" ) {
            continue;
          }
          if ( curLoop === result ) {
            result = [];
          }
          if ( $wnd.Expr.preFilter[ type ] ) {
            match = $wnd.Expr.preFilter[ type ]( match, curLoop, inplace, result, not);
            if ( !match ) {
              anyFound = found = true;
            } else if ( match === true ) {
              continue;
            }
          }
          if ( match ) {
            for ( var i = 0; (item = curLoop[i]) != null; i++ ) {
              if ( item ) {
                found = filter( item, match, i, curLoop );
                var pass = not ^ !!found;

                if ( inplace && found != null ) {
                  if ( pass ) {
                    anyFound = true;
                  } else {
                    curLoop[i] = false;
                  }
                } else if ( pass ) {
                  result.push( item );
                  anyFound = true;
                }
              }
            }
          }
          if ( found !== undefined ) {
            if ( !inplace ) {
              curLoop = result;
            }
            expr = expr.replace( $wnd.Expr.match[ type ], "" );
            if ( !anyFound ) {
              return [];
            }
            break;
          }
        }
      }
      // Improper expression
      if ( expr === old ) {
        if ( anyFound == null ) {
          @com.google.gwt.query.client.impl.research.SelectorEngineSizzleGwt::error(Ljava/lang/String;)(expr);
        } else {
          break;
        }
      }
      old = expr;
    }
    return curLoop;
  }-*/;

  public static native JavaScriptObject find(String expr, Node context) /*-{
    var set, match;
    if ( !expr ) {
      return [];
    }
    for ( var i = 0, l = $wnd.Expr.order.length; i < l; i++ ) {
      var type = $wnd.Expr.order[i], match;

      if ( (match = $wnd.Expr.leftMatch[ type ].exec( expr )) ) {
        var left = match[1];
        match.splice(1,1);

        if ( left.substr( left.length - 1 ) !== "\\" ) {
          match[1] = (match[1] || "").replace(/\\/g, "");
          set = $wnd.Expr.find[ type ]( match, context);
          if ( set != null ) {
            expr = expr.replace( $wnd.Expr.match[ type ], "" );
            break;
          }
        }
      }
    }
    if ( !set ) {
      set = context.getElementsByTagName("*");
    }
    return {set: set, expr: expr};
  }-*/;

  public static native String getText(Object elems) /*-{
    var ret = "", elem;
    for ( var i = 0; elems[i]; i++ ) {
      elem = elems[i];
      // Get the text from text nodes and CDATA nodes
      if ( elem.nodeType === 3 || elem.nodeType === 4 ) {
        ret += elem.nodeValue;
      // Traverse everything else, except comment nodes
      } else if ( elem.nodeType !== 8 ) {
        ret += @com.google.gwt.query.client.impl.research.SelectorEngineSizzleGwt::getText(Ljava/lang/Object;)(elem.childNodes);
      }
    }
    return ret;
  }-*/;

  public static native JsArray<Node> makeArray(NodeList<Node> array, JsArray<Node> results) /*-{
    var ret = results || [];
    if ( Object.prototype.toString.call(array) === "[object Array]" ) {
      Array.prototype.push.apply( ret, array );
    } else {
      if ( typeof array.length === "number" ) {
        for ( var i = 0, l = array.length; i < l; i++ ) {
          ret.push( array[i] );
        }
      } else {
        for ( var i = 0; array[i]; i++ ) {
          ret.push( array[i] );
        }
      }
    }
    return ret;
  }-*/;

  public static native JsArray<Element> posProcess(String selector, Node context) /*-{
    var tmpSet = [], later = "", match, root = context.nodeType ? [context] : context;
    // Position selectors must be done after the filter
    // And so must :not(positional) so we move all PSEUDOs to the end
    while ( (match = $wnd.Expr.match.PSEUDO.exec( selector )) ) {
      later += match[0];
      selector = selector.replace($wnd.Expr.match.PSEUDO, "" );
    }
    selector = $wnd.Expr.relative[selector] ? selector + "*" : selector;
    for ( var i = 0, l = root.length; i < l; i++ ) {
      @com.google.gwt.query.client.impl.research.SelectorEngineSizzleGwt::select(Ljava/lang/String;Lcom/google/gwt/dom/client/Node;Lcom/google/gwt/core/client/JsArray;Lcom/google/gwt/core/client/JsArray;)(selector, root[i], tmpSet, null);
    }
    return @com.google.gwt.query.client.impl.research.SelectorEngineSizzleGwt::filter(Ljava/lang/String;Lcom/google/gwt/core/client/JsArray;ZLjava/lang/Object;)( later, tmpSet, false );
  }-*/;

  private static native JsArray<Element> select(String selector, Node context, JsArray<Element> results, JsArray<Element> seed) /*-{
    results = results || [];
    var origContext = context = context || document;
    var parts = [], m, set, checkSet, extra, prune = true, soFar = selector;
    // Reset the position of the chunker regexp (start from head)
    while ( ($wnd.Expr.match.CHUNKER.exec(""), m = $wnd.Expr.match.CHUNKER.exec(soFar)) !== null ) {
      soFar = m[3];
      parts.push( m[1] );
      if ( m[2] ) {
        extra = m[3];
        break;
      }
    }
    if ( parts.length > 1 && $wnd.Expr.match.POS.exec( selector ) ) {
      if ( parts.length === 2 && $wnd.Expr.relative[ parts[0] ] ) {
        set = @com.google.gwt.query.client.impl.research.SelectorEngineSizzleGwt::posProcess(Ljava/lang/String;Lcom/google/gwt/dom/client/Node;)(parts[0] + parts[1], context);
      } else {
        set = $wnd.Expr.relative[ parts[0] ] ?
          [ context ] :
          @com.google.gwt.query.client.impl.research.SelectorEngineSizzleGwt::select(Ljava/lang/String;Lcom/google/gwt/dom/client/Node;Lcom/google/gwt/core/client/JsArray;Lcom/google/gwt/core/client/JsArray;)(parts.shift(), context, null, null);
        while ( parts.length ) {
          selector = parts.shift();
          if ( $wnd.Expr.relative[ selector ] ) {
            selector += parts.shift();
          }
          set = @com.google.gwt.query.client.impl.research.SelectorEngineSizzleGwt::posProcess(Ljava/lang/String;Lcom/google/gwt/dom/client/Node;)(selector, set);
        }
      }
    } else {
      // Take a shortcut and set the context if the root selector is an ID
      // (but not if it'll be faster if the inner selector is an ID)
      if ( !seed && parts.length > 1 && context.nodeType === 9 &&
          $wnd.Expr.match.ID.test(parts[0]) && !$wnd.Expr.match.ID.test(parts[parts.length - 1]) ) {
        var ret = @com.google.gwt.query.client.impl.research.SelectorEngineSizzleGwt::find(Ljava/lang/String;Lcom/google/gwt/dom/client/Node;)( parts.shift(), context);
        context = ret.expr ? @com.google.gwt.query.client.impl.research.SelectorEngineSizzleGwt::filter(Ljava/lang/String;Lcom/google/gwt/core/client/JsArray;ZLjava/lang/Object;)( ret.expr, ret.set, false )[0] : ret.set[0];
      }
      if ( context ) {
        var ret = seed ?
          { expr: parts.pop(), set: @com.google.gwt.query.client.impl.research.SelectorEngineSizzleGwt::makeArray(Lcom/google/gwt/dom/client/NodeList;Lcom/google/gwt/core/client/JsArray;)(seed, null) } :
          @com.google.gwt.query.client.impl.research.SelectorEngineSizzleGwt::find(Ljava/lang/String;Lcom/google/gwt/dom/client/Node;)( parts.pop(), parts.length === 1 && (parts[0] === "~" || parts[0] === "+") && context.parentNode ? context.parentNode : context);
        set = ret.expr ? @com.google.gwt.query.client.impl.research.SelectorEngineSizzleGwt::filter(Ljava/lang/String;Lcom/google/gwt/core/client/JsArray;ZLjava/lang/Object;)( ret.expr, ret.set, false ) : ret.set;
        if ( parts.length > 0 ) {
          checkSet = @com.google.gwt.query.client.impl.research.SelectorEngineSizzleGwt::makeArray(Lcom/google/gwt/dom/client/NodeList;Lcom/google/gwt/core/client/JsArray;)(set, null);
        } else {
          prune = false;
        }
        while ( parts.length ) {
          var cur = parts.pop(), pop = cur;
          if ( !$wnd.Expr.relative[ cur ] ) {
            cur = "";
          } else {
            pop = parts.pop();
          }
          if ( pop == null ) {
            pop = context;
          }
          $wnd.Expr.relative[ cur ]( checkSet, pop);
        }
      } else {
        checkSet = parts = [];
      }
    }
    if ( !checkSet ) {
      checkSet = set;
    }
    if ( !checkSet ) {
      @com.google.gwt.query.client.impl.research.SelectorEngineSizzleGwt::error(Ljava/lang/String;)(cur || selector);
    }
    if ( Object.prototype.toString.call(checkSet) === "[object Array]" ) {
      if ( !prune ) {
        results.push.apply( results, checkSet );
      } else if ( context && context.nodeType === 1 ) {
        for ( var i = 0; checkSet[i] != null; i++ ) {
          if ( checkSet[i] && (checkSet[i] === true || checkSet[i].nodeType === 1 && @com.google.gwt.query.client.impl.research.SelectorEngineSizzleGwt::contains(Ljava/lang/Object;Ljava/lang/Object;)(context, checkSet[i])) ) {
            results.push( set[i] );
          }
        }
      } else {
        for ( var i = 0; checkSet[i] != null; i++ ) {
          if ( checkSet[i] && checkSet[i].nodeType === 1 ) {
            results.push( set[i] );
          }
        }
      }
    } else {
      @com.google.gwt.query.client.impl.research.SelectorEngineSizzleGwt::makeArray(Lcom/google/gwt/dom/client/NodeList;Lcom/google/gwt/core/client/JsArray;)(checkSet, results);
    }
    if ( extra ) {
      @com.google.gwt.query.client.impl.research.SelectorEngineSizzleGwt::select(Ljava/lang/String;Lcom/google/gwt/dom/client/Node;Lcom/google/gwt/core/client/JsArray;Lcom/google/gwt/core/client/JsArray;)(extra, origContext, results, seed);
    }
    return results;
   }-*/;

  public SelectorEngineSizzleGwt() {
    createExpr();
  }

  public NodeList<Element> select(String selector, Node context) {
    JsArray<Element> results = JavaScriptObject.createArray().cast();
    return  JsUtils.unique(select(selector, context, results, null)).cast();
  }
}
