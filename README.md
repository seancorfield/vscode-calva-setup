# VS Code/Clover Setup

The files here represent my current [VS Code](https://code.visualstudio.com/) configuration for use with [Clover](https://marketplace.visualstudio.com/items?itemName=mauricioszabo.clover) **version 0.2.4 or later**.

The commands here will `tap>` all evaluations. If you have a [Portal UI](https://github.com/djblue/portal), possibly inside VS Code, running that will also render everything that is `tap>`'d into its UI.

* `config.cljs` contains all my enhancements (and belongs in `~/.config/clover/`),
* `keybindings.json` is my cross-platform key mappings for Clover and those `tap>` commands,
* `settings.json` is my user-level settings (consider this optional, but it does include a Clover setting).

## Installation

You can either clone this repo into a temporary directory and then copy `config.cljs` to your `~/.config/clover/` folder (create that folder if it doesn't already exist), and copy some or all of the two `.json` files into your VS Code user configuration directory (overwriting the default `keybindings.json` and `settings.json` files).

On a Mac, the VS Code user configuration directory is as shown below:

```bash
$ cd ~/Library/Application\ Support/Code/User
```

On Windows, the VS Code user configuration directory is likely to be:

```bash
> cd C:\Users\<username>\AppData\Roaming\Code\User
```

I run VS Code in Remote-WSL2 mode so my `config.cljs` file for Clover is in `~/.config/clover/` on Linux.

## Portal: Launch and Usage

### Prerequisites

When starting an instance of Portal, you must ensure that you have the following:

Portal itself must be included as a dependency. This can be done in your user configuration file (deps.edn or ~/.lein/profiles.clj for example) or in your project's configuration. If you're using Leiningen, it'll look like this:

`:dependencies [[djblue/portal "0.18.0"]]`

You must be configured to start a socket repl. That configuration can live in the same places as the dependency, and if you're using Leiningen, it'll look something like this:

`:jvm-opts ["-Dclojure.server.repl={:port 5555 :accept clojure.core.server/repl}"]}`

### Launching and Using Portal

Before you launch Portal, you must first start a repl and connect to it. Once that is up, you will use the following commands.

1. Connect to the running repl via Clover with: `ctrl-; y`
2. Start Portal with: `ctrl-; shift+p`
3. That’s it! You can now access values stored in portal by dereferencing the portal object in the dev namespace like so: `@dev/portal`. This object is created automatically by the startup command.

## Keymap

The additional commands _require_ Clojure 1.10 (because they assume `requiring-resolve` and `tap>`) and will fail on earlier versions. Portal supports Clojure 1.10's `datafy` and `nav`. If you need to work on an earlier project, you'll need to use Chlorine's default versions of the commands.

* `ctrl-; b` -- evaluate current form into `tap>`.
* `ctrl-; shift+b` -- evaluate current top-level form into `tap>`.
* `ctrl-; c` -- Clover's built-in break evaluation.
* `ctrl-; d` -- Clover's built-in show docs for var. See also `j` and `?` below.
* `ctrl-; shift+d` -- when a binding in `let` is highlighted (both the symbol and the expression to which it is bound), this creates a `def` so the symbol becomes available at the top level: useful for debugging parts of a function inside `let`.
* `ctrl-; e` -- Clover's built-in disconnect (from the REPL).
* `ctrl-; f` -- Clover's built-in load file.
* `ctrl-; j` -- treat the var at the cursor (or the current selection) as a Java class or instance, lookup the Java API docs for it, and produce that URL (assumes the class is part of the Java Standard Library or that Google can find it!); attempts to `slurp` the URL and send the returned HTML via `tap>` in a metadata wrapper that should render into Portal (taps just the URL if the `slurp` fails).
* `ctrl-; k` -- Clover's built-in clear console.
* `ctrl-; shift+k` -- Clear Portal's history.
* `ctrl-; n` -- send the current namespace (object) to `tap>` (my dot-clojure `dev.clj` customizes Portal to provide a list of public Vars).
* `ctrl-; shift+p` -- Start a Portal view inside VS Code, with some extra `dev` actions added (see below).
* `ctrl-; r` -- remove the current namespace's definitions: occasionally useful for cleaning up REPL state.
* `ctrl-; shift+r` -- reload the current namespace and all of its dependencies (uses `(require ,,, :reload-all)`: occasionally useful for cleaning up REPL state.
* `ctrl-; s` -- evaluate the selected code into `tap>` -- it can only be a single form (it will be used as the expression in a `let` binding).
* `ctrl-; shift+s` -- Clover's built-in show source for var.
* `ctrl-; t` -- run the current test (cursor can be anywhere in a `deftest` form, or `defspec`, `defexpect`, etc); displays the test success/failure as a popup notification (see the Clover console for full details).
* `ctrl-; v` -- send the current symbol as a var to `tap>` (my dot-clojure `dev.clj` customizes Portal to show metadata for a Var).
* `ctrl-; x` -- run all the tests in the current namespace and `tap>` the result summary as well as showing the summary in a popup notification.
* `ctrl-; shift+x` -- run all the tests in the "associated" namespace and `tap>` the result summary as well as showing the summary in a popup notification; if the current namespace is `foo.bar`, this will look for `foo.bar-test` or `foo.bar-expectations`.
* `ctrl-; y` -- Clover's built-in connect to Socket REPL.
* `ctrl-; shift+/` (i.e., `?`) -- for the var at the cursor, produce the  ClojureDocs URL as a `java.net.URL`; attempts to `slurp` the URL and send the returned HTML via `tap>` in a metadata wrapper that should render into Portal (taps just the URL if the `slurp` fails).
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

### Portal Custom Commands

The `ctrl+; shift-p` keybinding will add the following custom actions to Portal:

* `dev/->html` -- attempts to `slurp` the value (a filename or URL) and display it inline as HTML with a white background (so web pages are readable even in dark themes),
* `dev/->map` -- pours the value into a hash map (useful for dealing with Java `Properties` objects etc),
* `dev/->set` -- pours the value into a set,
* `dev/->vector` -- pours the value into a vector.
