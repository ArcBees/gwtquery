
Introduction
------------

GwtQuery is a jQuery-like API written in GWT, which allows GWT to be used in
progressive enhancement scenarios where perhaps GWT widgets are too
heavyweight. 

Currently, only a part of the jQuery API is written. Most CSS3 selectors are 
supported. People feel free to contribute patches to bring the API more in
line with jQuery.

This code is alpha, so expect it to break, and expect the API to change
in the future.

I would like the thank John Resig for writing jQuery, a kick ass library,
that is a pleasure to use, and I hope to capture that feeling in GWT. Also,
thanks to Robery Nyman for writing the fastest CSS Selector API
implementation (DOMAssistant), which I used as a guide for the GWT
impementation. GwtQuery is in large part, a port of DOMAssistant.

I am releasing this under the MIT License in the spirit for Robert Nyman's
choice, since the performance of this library wouldn't have been possible
without him.

Thanks,
Ray Cromwell <ray@timefire.com>
CTO, TimeFire
