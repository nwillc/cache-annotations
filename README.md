Cache Annotations
=================
Partial implementation of JSR 107 cache annotations employing AspectJ aspects to weave in the caching. Includes most
of the basic functionality. Implemented as a learning experience. These annotations should
work for any JSR 107 compliant implementation, however I've only tested them with my own `SimpleCache` implementation.

A general note, adding caching without these annotations is pretty straight forward. These annotations, by their very
nature introduce overhead. Every affected method call requires a certain amount of runtime investigation employing
reflection.  Usually caching is added for speed, so I'm not clear on the logic of avoiding some pretty straight forward
code at the cost of continuous overhead.

-----
[![ISC License](http://shields-nwillc.rhcloud.com/shield/tldrlegal?package=ISC)](http://shields-nwillc.rhcloud.com/homepage/tldrlegal?package=ISC)
[![Build Status](http://shields-nwillc.rhcloud.com/shield/travis-ci?path=nwillc&package=cache-annotations)](http://shields-nwillc.rhcloud.com/homepage/travis-ci?path=nwillc&package=cache-annotations)
[![Download](http://shields-nwillc.rhcloud.com/shield/jcenter?path=nwillc&package=cache-annotations)](http://shields-nwillc.rhcloud.com/homepage/jcenter?group=com.github.nwillc&package=cache-annotations&path=nwillc/maven/cache-annotations)
