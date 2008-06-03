package gwtquery.rebind;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.JParameter;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

import java.io.PrintWriter;

import gwtquery.client.Selector;

/**
 *
 */
public abstract class SelectorGeneratorBase extends Generator {

  private TreeLogger treeLogger;

  protected JClassType NODE_TYPE = null;

  public String generate(TreeLogger treeLogger,
      GeneratorContext generatorContext, String requestedClass)
      throws UnableToCompleteException {
    this.treeLogger = treeLogger;
    TypeOracle oracle = generatorContext.getTypeOracle();
    NODE_TYPE = oracle.findType("com.google.gwt.dom.client.Node");

    JClassType selectorType = oracle.findType(requestedClass);
    SourceWriter sw = getSourceWriter(treeLogger, generatorContext,
        selectorType.getPackage().getName(),
        selectorType.getSimpleSourceName() + getImplSuffix(), requestedClass);
    if (sw != null) {
      for (JMethod method : selectorType.getMethods()) {
        generateMethod(sw, method, treeLogger);
      }
      genGetAllMethod(sw, selectorType.getMethods(), treeLogger);
      sw.commit(treeLogger);
    }

    return selectorType.getPackage().getName() + "."
        + selectorType.getSimpleSourceName() + getImplSuffix();
  }

  protected String getImplSuffix() {
    return "Impl";
  }

  // used by benchmark harness
  private void genGetAllMethod(SourceWriter sw, JMethod[] methods,
      TreeLogger treeLogger) {
    sw.println("public DeferredGQuery[] getAllSelectors() {");
    sw.indent();
    sw.println(
        "DeferredGQuery[] dg = new DeferredGQuery[" + (methods.length) + "];");
    int i = 0;
    for (JMethod m : methods) {
      Selector selectorAnnotation = m.getAnnotation(Selector.class);
      if(selectorAnnotation == null) continue;
      String selector = selectorAnnotation.value();

      sw.println("dg[" + i + "]=new DeferredGQuery() {");
      sw.indent();
      sw.println(
          "public String getSelector() { return \"" + selector + "\"; }");
      sw.println("public GQuery eval(Node ctx) { return " + wrapJS(m, m.getName()
          + (m.getParameters().length == 0 ? "()" : "(ctx)")+"") + " ;}");
      sw.println("public NodeList<Element> array(Node ctx) { return "+("NodeList".equals(m.getReturnType().getSimpleSourceName()) ?
        (m.getName()
          + (m.getParameters().length == 0 ? "(); " : "(ctx); ")) : 
          "eval"+(m.getParameters().length == 0 ? "(null).get(); " : "(ctx).get(); "))+"}");
      
      i++;
      sw.outdent();
      sw.println("};");
    }
    sw.println("return dg;");
    sw.outdent();
    sw.println("}");
  }

  public void generateMethod(SourceWriter sw, JMethod method, TreeLogger logger)
      throws UnableToCompleteException {
      Selector selectorAnnotation = method.getAnnotation(Selector.class);
    if(selectorAnnotation == null) return;

    String selector = selectorAnnotation.value();
    JParameter[] params = method.getParameters();

    sw.indent();
    String retType = method.getReturnType().getParameterizedQualifiedSourceName();
    sw.print("public final "+retType+" "+method.getName());
    boolean hasContext = false;
    if (params.length == 0) {
      sw.print("()");
    } else if (params.length == 1) {
      JClassType type = params[0].getType().isClassOrInterface();
      if (type != null && type.isAssignableTo(NODE_TYPE)) {
        sw.print("(Node root)");
        hasContext = true;
      }
    }
    sw.println(" {");
    sw.indent();
    Selector sel = method.getAnnotation(Selector.class);

    // short circuit #foo
    if (sel != null && sel.value().matches("^#\\w+$")) {
      sw.println("return "+wrap(method, "JSArray.create(((Document)" + (hasContext ? "root" : "(Node)Document.get()")
          + ").getElementById(\"" + sel.value().substring(1) + "\"))")+";");
    }
    // short circuit FOO
    else if (sel != null && sel.value().matches("^\\w+$")) {
      sw.println("return "+wrap(method, "JSArray.create(((Element)"+(hasContext ? "root" : "(Node)Document.get()")
          + ").getElementsByTagName(\"" + sel.value() + "\"))")+";");
    } // short circuit .foo for browsers with native getElementsByClassName 
    else if (sel != null && sel.value().matches("^\\.\\w+$")
        && hasGetElementsByClassName()) {
      sw.println("return "+wrap(method, "JSArray.create(getElementsByClassName(\""
          + sel.value().substring(1) + "\", " + (hasContext ? "root" : "(Node)Document.get()")
          + "))")+";");
    } else {
      generateMethodBody(sw, method, logger, hasContext);
    }
    sw.outdent();
    sw.println("}");
    sw.outdent();
  }

  protected boolean hasGetElementsByClassName() {
    return false;
  }

  protected void debug(String s) {
//    System.err.println(s);
    treeLogger.log(TreeLogger.DEBUG, s, null);
  }

  protected boolean notNull(String s) {
    return s != null && !"".equals(s);
  }

  protected SourceWriter getSourceWriter(TreeLogger logger,
      GeneratorContext context, String packageName, String className,
      String... interfaceNames) {
    PrintWriter printWriter = context.tryCreate(logger, packageName, className);
    if (printWriter == null) {
      return null;
    }
    ClassSourceFileComposerFactory composerFactory
        = new ClassSourceFileComposerFactory(packageName, className);
    composerFactory.setSuperclass("gwtquery.client.SelectorEngine");
    composerFactory.addImport("com.google.gwt.core.client.GWT");
    composerFactory.addImport("gwtquery.client.*");
//    composerFactory.addImport("gwtquery.client.JSArray");

    composerFactory.addImport("com.google.gwt.dom.client.*");
    for (String interfaceName : interfaceNames) {
      composerFactory.addImplementedInterface(interfaceName);
    }

    return composerFactory.createSourceWriter(context, printWriter);
  }

  protected String wrap(JMethod method, String expr) {
    if("NodeList".equals(method.getReturnType().getSimpleSourceName())) {
      return expr;
    }
    else {
      return "new GQuery("+expr+")";
    }
    
  }
  
  protected String wrapJS(JMethod method, String expr) {
    if("GQuery".equals(method.getReturnType().getSimpleSourceName())) {
      return expr;
    }
    else {
      return "new GQuery("+expr+")";
    }
    
  }
  
  protected abstract void generateMethodBody(SourceWriter sw, JMethod method,
      TreeLogger logger, boolean hasContext) throws UnableToCompleteException;
}
