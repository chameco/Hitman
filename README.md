Hitman [![Dependency Status](http://www.versioneye.com/clojure/hitman:hitman/0.1.0/badge.png)](http://www.versioneye.com/clojure/hitman:hitman/0.1.0)
======

Hitman is a Markdown parser, currently implemented in <100 lines of Clojure using [Instaparse](https://github.com/Engelberg/instaparse) and [Hiccup](https://github.com/weavejester/hiccup). There are a few features of Markdown that aren't yet supported (such as reference-style links and images, and Atx-style headers), but it generally works reasonably well. Obviously, there are some quirks to work out, but it's usable in its current state. If nothing else, I hope people can learn a bit about the fantastic Instaparse by looking at the code, as that was my own primary motivation for creating this.

Installation
------

`git clone git@github.com:chameco/Hitman.git`

It's also on [Clojars](https://clojars.org/hitman). Just add `[hitman "0.1.0"]` to your Leiningen dependencies.

Usage
-----

You can either run in place using Leiningen, or create a jar. You know the drill folks.

    $ lein run test.txt

will write output to `test.html`. Another option is

    $ lein uberjar
    $ java -jar target/hitman-0.1.0-standalone.jar test.txt

If you're going to use it as a library, just call `(markdown-to-html *string*)`.

License
-----

Copyright © 2013 Samuel Breese

Distributed under the Eclipse Public License, the same as Clojure.
