# VS Code/Clover Setup

The files here represent my current [VS Code](https://code.visualstudio.com/) configuration for use with [Clover](https://marketplace.visualstudio.com/items?itemName=mauricioszabo.clover) **version 0.2.1 or later**.

The commands here will `submit` all evaluations to [Cognitect's REBL](https://github.com/cognitect-labs/REBL-distro) if it is on your classpath, otherwise it `tap>`'s all evaluations. If you have a [Reveal REPL](https://github.com/vlaaad/reveal) running that will render everything that is `tap>`'d into its UI. If you have a [Portal UI](https://github.com/djblue/portal) running that will also render everything that is `tap>`'d into its UI.

* `config.cljs` contains all my enhancements (and belongs in `~/.config/clover/`),
* `keybindings.json` is my cross-platform key mappings for Clover and those REBL/`tap>` commands,
* `settings.json` is my user-level settings (consider this optional, but it does include a Clover setting).

## Installation

You can either clone this repo into a temporary directory and then copy `config.cljs` to your `~/.config/clover/` folder (create that folder if it doesn't already exist), and copy the two `.json` files into your VS Code user configuration directory (overwriting the default `keybindings.json` and `settings.json` files), or you can clone it on top of your existing VS Code user configuration directory, so that you can keep it updated to match this repo by pulling new changes as desired, and then symlink `config.cljs` into `~/.config/clover/`.

On a Mac, the VS Code user configuration directory is as shown below:

```bash
$ cd ~/Library/Application\\ Support/Code/User
$ git init
$ git remote add origin https://github.com/seancorfield/vscode-clover-setup.git
$ git fetch
$ git checkout develop -f
$ mkdir -p ~/.config/clover
$ ln -s `pwd`/config.cljs ~/.config/clover/config.cljs
```

That last line will overwrite any existing versions of those three files.

To update your files to the latest versions from this repo:

```bash
$ cd ~/Library/Application\ Support/Code/User
$ git pull
```

On Windows, the VS Code user configuration directory is likely to be:

```bash
C:\\Users\\<username>\\AppData\\Roaming\\Code\\User
```

I run VS Code in Remote-WSL2 mode so my `config.cljs` file for Clover is in `~/.config/clover/` on Linux so the symlink works as above. If you run VS Code with Clover in local mode, you'll have to copy `config.cljs` and keep it in sync manually.

## Keymap

The additional commands _require_ Clojure 1.10 (because they assume `requiring-resolve` and `tap>`) and will fail on earlier versions. REBL, Reveal, and Portal both support Clojure 1.10's `datafy` and `nav`. If you need to work on an earlier project, you'll need to use Chlorine's default versions of the commands.

* `ctrl-; b` -- evaluate current form into REBL/`tap>`.
* `ctrl-; shift+b` -- evaluate current top-level form into REBL/`tap>`.
* `ctrl-; c` -- Clover's built-in break evaluation.
* `ctrl-; d` -- Clover's built-in show docs for var. See also `j` and `?` below.
* `ctrl-; shift+d` -- when a binding in `let` is highlighted (both the symbol and the expression to which it is bound), this creates a `def` so the symbol becomes available at the top level: useful for debugging parts of a function inside `let`.
* `ctrl-; e` -- Clover's built-in disconnect (from the REPL).
* `ctrl-; f` -- Clover's built-in load file.
* `ctrl-; j` -- treat the var at the cursor (or the current selection) as a Java class or instance, lookup the Java API docs for it, and display that web page in REBL (assumes the class is part of the Java Standard Library) or `tap>` the `java.net.URL` object (which can be browsed in Reveal or clicked in Portal).
* `ctrl-; k` -- Clover's built-in clear console.
* `ctrl-; n` -- inspect the current namespace in REBL (Reveal and Portal do not currently do anything special with a namespace).
* `ctrl-; r` -- remove the current namespace's definitions: useful for cleaning up REPL state.
* `ctrl-; shift+r` -- reload the current namespace and all of its dependencies (uses `(require ,,, :reload-all)`: useful for cleaning up REPL state.
* `ctrl-; s` -- evaluate the selected code into REBL/`tap>` -- it can only be a single form (it will be used as the expression in a `let` binding).
* `ctrl-; shift+s` -- Clover's built-in show source for var.
* `ctrl-; t` -- Clover's built-in run test for var.
* `ctrl-; v` -- inspect the current symbol in REBL (as a var; Reveal and Portal do not currently do anything special with vars, unlike REBL).
* `ctrl-; x` -- Clover's built-in run tests in (current) namespace.
* `ctrl-; shift+x` -- If the current namespace is `foo.bar`, attempt to run tests in either `foo.bar-test` or `foo.bar-expectations`, display the test results in a popup (as per Clover's built-in test runner), display that summary inline as a hash map, and submit it to REBL/`tap>`.
* `ctrl-; y` -- Clover's built-in connect to Socket REPL.
* `ctrl-; shift+/` (i.e., `?`) -- for the var at the cursor, display the ClojureDocs web page for it in REBL (assumes the symbol is part of Clojure itself) or `tap>` the `java.net.URL` object (which can be browsed in Reveal or clicked in Portal).
* `ctrl-; .` -- Clover's built-in go to var definition.
