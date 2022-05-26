# VS Code Setup

The files here represent my current [VS Code](https://code.visualstudio.com/) configuration for use with [Calva](https://marketplace.visualstudio.com/items?itemName=betterthantomorrow.calva) and [Joyride](https://marketplace.visualstudio.com/items?itemName=betterthantomorrow.joyride).

The `settings.json` file makes heavy use of `customREPLCommandSnippets` to provide a number of `tap>` evaluations and some [Portal UI](https://github.com/djblue/portal) commands.

* `keybindings.json` is my cross-platform key mappings,
* `settings.json` is my user-level settings (consider this optional, but the `customREPLCommandSnippets` are the interesting part).

## Installation

You can either clone this repo into a temporary directory and then copy either or both of `.json` files into your VS Code user configuration directory (overwriting the default `keybindings.json` and `settings.json` files), or you can manually update your files with whatever bits of my files you like.

In addition, there are some Joyride scripts (in `joyride/scripts`) that you can copy into either your user or workspace Joyride `scripts` folder as desired. See the **Joyride** section below for details.

On a Mac, the VS Code user configuration directory is as shown below:

```bash
$ cd ~/Library/Application\ Support/Code/User
```

On Windows, the VS Code user configuration directory is likely to be:

```bash
> cd C:\Users\<username>\AppData\Roaming\Code\User
```

## Portal: Launch and Usage

### Prerequisites

When starting an instance of Portal, you must ensure that you have the following:

Portal itself must be included as a dependency. This can be done in your user configuration file (`deps.edn` or `~/.lein/profiles.clj` for example) or in your project's configuration. If you're using Leiningen, it'll look like this:

`:dependencies [[djblue/portal "RELEASE"]]`

For `deps.edn`, it will be `djblue/portal {:mvn/version "RELEASE"}` in `:extra-deps` under an alias. If you're using my user `deps.edn` file from my [`dot-clojure`](https://github.com/seancorfield/dot-clojure) repo, you can just add the `:portal` alias to your CLI command to get the latest Portal included.

### Launching and Using Portal

Before you launch Portal, you must first start a REPL and connect to it. Once that is up and connected in Calva, you can use the following custom REPL command:

* `ctrl+alt+space p` -- launch Portal inside VS Code

The following additional custom REPL commands are available for Portal:

* `ctrl+alt+space k` -- clear the Portal history
* `ctrl+alt+space 1` -- expand/collapse the first level of the most recent `tap>`'d value in Portal; `2` and `3` affect the second and third level respectively

You can also access values stored in portal by dereferencing the `portal` object in the `dev` namespace like so: `@dev/portal`. This object is created automatically by the launch command snippet.

See the custom REPL commands below for convenient `tap>` functionality.

## Custom REPL Command Snippets and `tap>`

In addition to the five custom REPL commands snippets mentioned above for Portal,
this `settings.json` file includes the following:

* `ctrl+alt+space a` -- add dependencies to the running REPL; sends the enclosing form to `clojure.tools.deps.alpha.repl/add-libs`; intended to be used when the cursor is inside a dependencies hash map (i.e., inside `deps.edn`, on a library name, rather than inside its coordinates).
* `ctrl+alt+space d` -- when a binding in `let` is highlighted (both the symbol and the expression to which it is bound), this creates a `def` so the symbol becomes available at the top level: useful for debugging parts of a function inside `let`.
* `ctrl+alt+space n` -- `tap>` a hash map of public Vars from the current namespace.
* `ctrl+alt+space c` -- run the current test and `tap>` any output.
* `ctrl+alt+space t` -- run all the tests in the current namespace and `tap>` the result summary.
* `ctrl+alt+space x` -- run all the tests in the "associated" namespace and `tap>` the result summary; if the current namespace is `foo.bar`, this will look for `foo.bar-test` or `foo.bar-expectations`.
* `ctrl+alt+space z` -- zap (remove) the current namespace's definitions: occasionally useful for cleaning up REPL state; this unaliases/unmaps all the symbols in the namespace _without destroying the namespace itself_, leaving it "empty" so you can load the file from disk again so it is fully-sync'd (`ctrl+alt+c enter`).

Several of these command snippets _require_ Clojure 1.10 or later (because they assume `requiring-resolve` and `tap>`) and will fail on earlier versions. Portal supports Clojure 1.10's `datafy` and `nav`.

## Joyride Scripts

The following scripts perform tasks that my old Clover configuration used to provide. You'll need Joyride installed in VS Code and you'll need at least v2.0.277 of Calva since these scripts depend on the recently-added [Calva Extension API](https://calva.io/api/).

* `clojuredocs.cljs` -- with a Clojure symbol selected, this will open VS Code's Simple Browser, directly inside VS Code, at the corresponding [ClojureDocs page](https://clojuredocs.org).
* `javadoc.cljs` -- with a Java class name selected, or a Clojure expression selected, this will open VS Code's Simple Browser, directly inside VS Code, at the corresponding (Oracle) JavaDoc page for the class that the selection resolves to. If the underlying `javadoc-url` function doesn't recognize the class, it produces a Google "I feel lucky" URL that will be opened in an _external_ browser instead (since Simple Browser cannot open Google's site).

> Note: both `clojuredocs.cljs` and `javadoc.cljs` assume you have a Clojure nREPL connected in Calva to perform symbol/class resolution!

My `keybindings.json` file has key bindings for these that are intended to match (but override) what would be custom REPL command snippet key bindings, to make these Joyride scripts feel more like snippets:

* `ctrl+alt+space j` -- runs `javadoc.cljs` as a User-level Joyride script.
* `ctrl+alt+space /` -- runs `clojuredocs.cljs` as a User-level Joyride script (in my Clover setup, this used to be `ctrl+; shift+/`, i,e., `?`, but `shift+/` would be a bit unwieldy as a `ctrl+alt+space` chord).

## Keymap

Other key bindings that you may or may not find useful:

* `ctrl+alt+e ctrl+alt+j` -- join window groups in VS Code (I often use this after starting the REPL or splitting a window).
* `ctrl+alt+e right` -- move the current window to the next group to the right in VS Code (I often use this after starting the REPL or splitting a window).
* `ctrl+t ctrl+f` -- switch focus to the terminal window in VS Code.
* `ctrl+t ctrl+t` -- open a terminal window in VS Code.
* `ctrl+l ctrl+l` -- convert the selection/word at the cursor to lowercase.
* `ctrl+u ctrl+u` -- convert the selection/word at the cursor to uppercase.
* `ctrl+alt+k ctrl+alt+t` -- toggle between Clojure implementation and test files.

These are intended to provide a consistent experience across macOS _and_ Windows:

* `ctrl+right` -- (Calva's) paredit forward s-exp.
* `alt+right` -- cursor word right.
* `ctrl+left` -- (Calva's) paredit backward s-exp.
* `alt+left` -- cursor word left.
* `ctrl+j` -- join lines (this is the default on macOS but not Windows).

If you use Atlassian/Jira/BitBucket:

* `ctrl+alt+a ctrl+alt+b` -- switch to BitBucket PRs.
* `ctrl+alt+a ctrl+alt+j` -- switch to Jira issues.
