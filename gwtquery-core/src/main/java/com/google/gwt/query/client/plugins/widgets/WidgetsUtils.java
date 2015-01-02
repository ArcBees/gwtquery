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
package com.google.gwt.query.client.plugins.widgets;

import static com.google.gwt.query.client.GQuery.$;

import com.google.gwt.dom.client.BodyElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.WidgetCollection;

import java.util.Iterator;

/**
 * WidgetsUtils.
 */
public class WidgetsUtils {

  static final String[] appendingTags = {
      "td", "th", "li"};

  /**
    * Append a widget to a dom element, and hide it.
    * Element classes will be copied to the new widget.
    */
  public static void hideAndAfter(Element e, Widget widget) {
    assert e != null && widget != null;
    if ($(e).widget() != null && $(e).widget().isAttached()) {
      replaceWidget($(e).widget(), widget, false);
    } else {
      detachWidget(widget);
      hideAndAfter(e, widget.getElement());
      attachWidget(widget, getFirstParentWidget(widget));
    }
  }

  /**
   * Test if the tag name of the element is one of tag names given in parameter.
   *
   * @param tagNames
   * @return
   */
  public static boolean matchesTags(Element e, String... tagNames) {

    assert e != null : "Element cannot be null";

    StringBuilder regExp = new StringBuilder("^(");
    int tagNameLenght = tagNames != null ? tagNames.length : 0;
    for (int i = 0; i < tagNameLenght; i++) {
      regExp.append(tagNames[i].toUpperCase());
      if (i < tagNameLenght - 1) {
        regExp.append("|");
      }
    }
    regExp.append(")$");

    return e.getTagName().toUpperCase().matches(regExp.toString());
  }

  /**
   * Replace a dom element by a widget.
   * Old element classes will be copied to the new widget.
   */
  public static void replaceOrAppend(Element e, Widget widget) {
    assert e != null && widget != null;
    if ($(e).widget() != null && $(e).widget().isAttached()) {
      replaceWidget($(e).widget(), widget, true);
    } else {
      detachWidget(widget);
      replaceOrAppend(e, widget.getElement());
      attachWidget(widget, getFirstParentWidget(widget));
    }
  }

  private static Widget getFirstParentWidget(Widget w) {
    Element e = w.getElement().getParentElement();
    BodyElement body = Document.get().getBody();
    while ((e != null) && (body != e)) {
      if (Event.getEventListener(e) != null) {
        Widget p = $(e).widget();
        if (p != null) {
          return p;
        }
      }
      e = e.getParentElement();
    }
    return null;
  }

  private static void hideAndAfter(Element oldElement, Element newElement) {
    assert oldElement != null && newElement != null;
    GQuery.$(oldElement).hide().after(newElement);
    String c = oldElement.getClassName();
    if (!c.isEmpty()) {
      newElement.addClassName(c);
    }
  }

  /**
   * If the <code>oldElement</code> is a td, th, li tags, the new element will replaced its content.
   * In other cases, the <code>oldElement</code> will be replaced by the <code>newElement</code>
   *  and the old element classes will be copied to the new element.
   */
  private static void replaceOrAppend(Element oldElement, Element newElement) {
    assert oldElement != null && newElement != null;

    if (matchesTags(oldElement, appendingTags)) {
      GQuery.$(oldElement).html("").append(newElement);
    } else {
      GQuery.$(oldElement).replaceWith(newElement);

      // copy class
      String c = oldElement.getClassName();
      if (!c.isEmpty()) {
        newElement.addClassName(c);
      }
      // copy id
      newElement.setId(oldElement.getId());
      // ensure no duplicate id
      oldElement.setId("");
    }
  }

  private static void replaceWidget(Widget oldWidget, Widget newWidget, boolean remove) {
    Widget parent = oldWidget.getParent();
    boolean removed = false;
    // TODO: handle tables
    if (parent instanceof HTMLPanel) {
      ((HTMLPanel) parent).addAndReplaceElement(newWidget,
          (com.google.gwt.dom.client.Element) oldWidget.getElement());
      if (!remove) {
        $(newWidget).before($(oldWidget));
      }
      removed = true;
    } else if (parent instanceof ComplexPanel) {
      ((ComplexPanel) parent).add(newWidget);
    } else if (parent instanceof SimplePanel) {
      ((SimplePanel) parent).setWidget(newWidget);
    } else if (parent instanceof Panel) {
      ((Panel) parent).add(newWidget);
    } else {
      assert false : "Can not replace an attached widget whose parent is a "
          + parent.getClass().getName();
    }
    if (remove && !removed && oldWidget.isAttached()) {
      oldWidget.removeFromParent();
    } else {
      oldWidget.setVisible(false);
    }
  }

  /**
   * Attach a widget to the GWT widget list. Normally used when GQuery
   * creates widgets wrapping existing dom elements.
   * It does nothing if the widget is already attached to another widget.
   *
   * @param widget to attach
   * @param firstParentWidget the parent widget,
   *   If it is null and it is not inside any other widget, we just add
   *   the widget to the gwt detach list
   */
  public static void attachWidget(Widget widget, Widget firstParentWidget) {
    if (widget != null && widget.getParent() == null) {
      if (firstParentWidget == null) {
        firstParentWidget = getFirstParentWidget(widget);
        if (firstParentWidget == null) {
          RootPanel.detachOnWindowClose(widget);
          widgetOnAttach(widget);
        } else {
          attachWidget(widget, firstParentWidget);
        }
      } else if (firstParentWidget instanceof HTMLPanel) {
        HTMLPanel h = (HTMLPanel) firstParentWidget;
        Element p = widget.getElement().getParentElement();
        if (p != null) {
          h.add(widget, p);
        } else {
          h.add(widget);
        }
      } else if (firstParentWidget instanceof HasOneWidget) {
        ((HasOneWidget) firstParentWidget).setWidget(widget);
      } else if (firstParentWidget instanceof HasWidgets) {
        try {
          ((HasWidgets) firstParentWidget).add(widget);
        } catch (UnsupportedOperationException e) {
          // Some widgets like 'table' has no implementation of 'add(widget)'
          widgetSetParent(widget, firstParentWidget);
        }
      } else {
        widgetSetParent(widget, firstParentWidget);
      }
    }
  }

  /**
   * Remove a widget from the detach list.
   */
  public static void detachWidget(final Widget widget) {
    if (widget != null) {
      widget.removeFromParent();
    }
  }

  /**
   * This method detach a widget of its parent without doing a physical
   * detach (DOM manipulation).
   */
  public static void doLogicalDetachFromHtmlPanel(Widget w) {
    Widget parent = w.getParent();

    if (parent instanceof HTMLPanel) {
      complexPanelGetChildren((HTMLPanel) parent).remove(w);
      widgetSetParent(w, null);
    } else {
      throw new IllegalStateException(
          "You can only use this method to detach a child from an HTMLPanel");
    }
  }

  /**
   * Return children of the first widget's panel.
   */
  public static Iterator<Widget> getChildren(Widget w) {
    if (w instanceof Panel) {
      return ((Panel) w).iterator();
    }
    if (w instanceof Composite) {
      return getChildren(compositeGetWidget((Composite) w));
    }
    return null;
  }

  /**
   * Return the first widget parent of the element, or null if it is not
   * attached to any widget yet.
   */
  private static Widget getFirstParentWidgetElement(Element element) {
    return $(element).parents().widget();
  }

  private static native void widgetOnAttach(Widget w) /*-{
    w.@com.google.gwt.user.client.ui.Widget::onAttach()();
  }-*/;

  private static native void widgetSetParent(Widget w, Widget p) /*-{
   w.@com.google.gwt.user.client.ui.Widget::setParent(Lcom/google/gwt/user/client/ui/Widget;)(p);
  }-*/;

  private static native Widget compositeGetWidget(Composite w) /*-{
   return w.@com.google.gwt.user.client.ui.Composite::getWidget()();
  }-*/;

  private static native WidgetCollection complexPanelGetChildren(ComplexPanel w) /*-{
   return w.@com.google.gwt.user.client.ui.ComplexPanel::getChildren()();
  }-*/;
}
