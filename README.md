# VS Code/Clover Setup

The files here represent my current [VS Code](https://code.visualstudio.com/) configuration for use with [Clover](https://marketplace.visualstudio.com/items?itemName=mauricioszabo.clover) **version 0.2.4 or later**.

The commands here will `tap>` all evaluations. If you have a [Reveal REPL](https://github.com/vlaaad/reveal) running that will render everything that is `tap>`'d into its UI. If you have a [Portal UI](https://github.com/djblue/portal) running that will also render everything that is `tap>`'d into its UI.

If you want to use this with Cognitect's REBL, you'll need to comment/uncomment the relevant `wrap-in-tap` function in `config.cljs`.

* `config.cljs` contains all my enhancements (and belongs in `~/.config/clover/`),
* `keybindings.json` is my cross-platform key mappings for Clover and those REBL/`tap>` commands,
* `settings.json` is my user-level settings (consider this optional, but it does include a Clover setting).

## Installation

You can either clone this repo into a temporary directory and then copy `config.cljs` to your `~/.config/clover/` folder (create that folder if it doesn't already exist), and copy the two `.json` files into your VS Code user configuration directory (overwriting the default `keybindings.json` and `settings.json` files), or you can clone it on top of your existing VS Code user configuration directory, so that you can keep it updated to match this repo by pulling new changes as desired, and then symlink `config.cljs` into `~/.config/clover/`.

On a Mac, the VS Code user configuration directory is as shown below:

```bash
$ cd ~/Library/Application\ Support/Code/User
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
C:\Users\<username>\AppData\Roaming\Code\User
```

I run VS Code in Remote-WSL2 mode so my `config.cljs` file for Clover is in `~/.config/clover/` on Linux so the symlink works as above. If you run VS Code with Clover in local mode, you'll have to copy `config.cljs` and keep it in sync manually.

## Keymap

The additional commands _require_ Clojure 1.10 (because they assume `requiring-resolve` and `tap>`) and will fail on earlier versions. REBL, Reveal, and Portal both support Clojure 1.10's `datafy` and `nav`. If you need to work on an earlier project, you'll need to use Chlorine's default versions of the commands.

* `ctrl-; b` -- evaluate current form into `tap>`.
* `ctrl-; shift+b` -- evaluate current top-level form into `tap>`.
* `ctrl-; c` -- Clover's built-in break evaluation.
* `ctrl-; d` -- Clover's built-in show docs for var. See also `j` and `?` below.
* `ctrl-; shift+d` -- when a binding in `let` is highlighted (both the symbol and the expression to which it is bound), this creates a `def` so the symbol becomes available at the top level: useful for debugging parts of a function inside `let`.
* `ctrl-; e` -- Clover's built-in disconnect (from the REPL).
* `ctrl-; f` -- Clover's built-in load file.
* `ctrl-; j` -- treat the var at the cursor (or the current selection) as a Java class or instance, lookup the Java API docs for it, and display that web page (assumes the class is part of the Java Standard Library or that Google can find it!) by sending a `java.net.URL` object to `tap>` (which can be browsed in Reveal or clicked in Portal).
* `ctrl-; k` -- Clover's built-in clear console.
* `ctrl-; n` -- send the current namespace (object) to `tap>` (my dot-clojure `dev.clj` customizes Reveal to provide a namespace browser view).
* `ctrl-; r` -- remove the current namespace's definitions: occasionally useful for cleaning up REPL state.
* `ctrl-; shift+r` -- reload the current namespace and all of its dependencies (uses `(require ,,, :reload-all)`: occasionally useful for cleaning up REPL state.
* `ctrl-; s` -- evaluate the selected code into `tap>` -- it can only be a single form (it will be used as the expression in a `let` binding).
* `ctrl-; shift+s` -- Clover's built-in show source for var.
* `ctrl-; t` -- run the current test (cursor can be anywhere in a `deftest` form, or `defspec`, `defexpect`, etc); displays the test success/failure as a popup notification (see the Clover console for full details).
* `ctrl-; v` -- send the current symbol as a var to `tap>` (my dot-clojure `dev.clj` customizes Reveal to provide a Var browser view).
* `ctrl-; x` -- run all the tests in the current namespace and `tap>` the result summary as well as showing the summary in a popup notification.
* `ctrl-; shift+x` -- run all the tests in the "associated" namespace and `tap>` the result summary as well as showing the summary in a popup notification; if the current namespace is `foo.bar`, this will look for `foo.bar-test` or `foo.bar-expectations`.
* `ctrl-; y` -- Clover's built-in connect to Socket REPL.
* `ctrl-; shift+/` (i.e., `?`) -- for the var at the cursor, send the  ClojureDocs URL as a `java.net.URL` to `tap>` (which can be browsed in Reveal or clicked in Portal).
* `ctrl-; .` -- Clover's built-in go to var definition.

If you have the `add-lib3` branch of `org.clojure/tools.deps.alpha` on your classpath:

* `ctrl-; shift+a` -- send current form to `clojure.tools.deps.alpha.repl/add-libs`; intended to be used when the current block is a dependencies hash map (i.e., inside `deps.edn`).

Other key bindings that you may or may not find useful:

* `ctrl+alt+e ctrl+alt+j` -- join window groups in VS Code (I often use this after starting the REPL or splitting a window).

These are intended to provide a consistent experience across macOS _and_ Windows:

* `ctrl+right` -- (Calva's) paredit forward s-exp.
* `alt+right` -- cursor word right.
* `ctrl+left` -- (Calva's) paredit backward s-exp.
* `alt+left` -- cursor word left.
* `ctrl+j` -- join lines (this is the default on macOS but not Windows).

If you use Atlassian/Jira/BitBucket:

* `ctrl+alt+a ctrl+alt+b` -- switch to BitBucket PRs.
* `ctrl+alt+a ctrl+alt+j` -- switch to Jira issues.
