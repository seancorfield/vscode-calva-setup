# VS Code Setup

The files here represent my current [VS Code](https://code.visualstudio.com/) configuration for use with [Calva](https://marketplace.visualstudio.com/items?itemName=betterthantomorrow.calva) and [Joyride](https://marketplace.visualstudio.com/items?itemName=betterthantomorrow.joyride).

> Note: Some of the code here may well assume you are using the latest version of Calva and Joyride -- I tend to update my extensions regularly and also update my configuration to take advantage of new features.

* `keybindings.json` is my cross-platform key mappings,
* `settings.json` is my user-level settings (consider this optional -- the `calva/config.edn` file with the `customREPLCommandSnippets` is the interesting part).

## Installation

You can either clone this repo into a temporary directory and then copy either or both of `.json` files into your VS Code user configuration directory (overwriting the default `keybindings.json` and `settings.json` files), or you can manually update your files with whatever bits of my files you like.

Copy (or merge) the `calva/config.edn` file's `customREPLCommandSnippets` into your
`~/.config/calva/config.edn` file for VS Code to find them (requires Calva 2.0.307 or later!).
These `customREPLCommandSnippets` provide a number of `tap>` evaluations and some [Portal UI](https://github.com/djblue/portal) commands.

**Requires Portal 0.37.1 or later**

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

**Requires Portal 0.37.1 or later**

Will work both with and without the `portal.nrepl/wrap-portal` middleware.

The REPL snippet that launches Portal inside VS Code will launch **two**
Portal windows:
* one called `** logging **` which tracks all Portal middleware evaluations and, if you use my `:dev/repl` alias from my [`dot-clojure`](https://github.com/seancorfield/dot-clojure) repo, all `clojure.tools.logging` output
* one named for the directory it is opened in which tracks all plain `tap>` calls

### Prerequisites

When starting an instance of Portal, you must ensure that you have the following:

Portal itself must be included as a dependency. This can be done in your user configuration file (`deps.edn` or `~/.lein/profiles.clj` for example) or in your project's configuration. If you're using Leiningen, it'll look like this:

`:dependencies [[djblue/portal "RELEASE"]]`

For `deps.edn`, it will be `djblue/portal {:mvn/version "RELEASE"}` in `:extra-deps` under an alias. If you're using my user `deps.edn` file from my [`dot-clojure`](https://github.com/seancorfield/dot-clojure) repo, you can just add the `:portal` alias to your CLI command to get the latest Portal included.

> Note: On Windows/WSL2, you may need `localhostForwarding=true` added to the `[wsl2]` section of your `.wslconfig` file for the VS Code / Portal server connection to work correctly.

### Launching and Using Portal

Before you launch Portal, you must first start a REPL and connect to it. Once that is up and connected in Calva, you can use the following custom REPL command:

* `ctrl+alt+space p` -- launch Portal inside VS Code; this uses a custom `submit` listener for `tap>` that tracks middleware output + logging and regular `tap>` calls in separate atoms, one behind each of the two windows opened

The following additional custom REPL commands are available for Portal, and they all operate on the regular result window (not the middleware output/logging window):

* `ctrl+alt+space k` -- clear the Portal history
* `ctrl+alt+space 0` -- cycle the latest value in the Portal history through each of the viewers in turn
* `ctrl+alt+space 1` -- expand/collapse the first level of the most recent `tap>`'d value in Portal; `2`, `3`, `4`, and `5` affect the second thru fifth level respectively

You can also access values stored in portal by dereferencing the `portal` object in the `dev` namespace like so: `@dev/portal`. This object is created automatically by the launch command snippet.

Similarly, `(first @dev/my-taps)` is always the most recent value `tap>`'d and the following
custom REPL command lets you manipulate it:

* `ctrl+alt+space q` -- pops open a REPL input prompt into which you can type arbitrary code which will be evaluated and `tap>`'d

If that code includes `*v`, it will be automatically replaced with `(first @dev/my-taps)`
so you can use the following workflow:

* `tap>` any value into Portal via any method
* `ctrl+alt+space q` and type some expression involving `*v`
* press enter to have that evaluated and `tap>`'d
* rinse and repeat!

See the custom REPL commands below for convenient `tap>` functionality.

## Custom REPL Command Snippets and `tap>`

In addition to the custom REPL commands snippets mentioned above for Portal,
this `settings.json` file includes the following:

* `ctrl+alt+space a` -- add dependencies to a running REPL by synchronizing with your `deps.edn` file; this pops open a REPL input prompt into which you can type one or more aliases, and then it uses `clojure.repl.deps/sync-deps` (Clojure 1.12.0 Alpha 2) to load any new dependencies that have been added to your `deps.edn`.
* `ctrl+alt+space c` -- run the current test (use `ctrl+alt+space o` to see output/report) and display a summary of results.
* `ctrl+alt+space d` -- when a binding in `let` is highlighted (both the symbol and the expression to which it is bound), this creates a `def` so the symbol becomes available at the top level: useful for debugging parts of a function inside `let`.
* `ctrl+alt+space e` -- `tap>` the last exception thrown (`*e`)
* `ctrl+alt+space i` -- when a symbol is highlighted, create a `def` from it bound to an input value provided by a REPL prompt in Calva: useful for debugging parts of a function (by defining argument values or other symbols).
* `ctrl+alt+space l` -- "lift" the result out of the most recent `** logging **` window entry into the regular `tap>` window for easier manipulation.
* `ctrl+alt+space n` -- `tap>` a hash map of public Vars from the current namespace.
* `ctrl+alt+space o` -- where `l` lifts the _result_ from `** logging **`, this lifts the standard output (and any test reports): useful when you want to see output produced by an evaluation or test results (from `ctrl+alt+space c` or `t` or `x`)
* `ctrl+alt+space r` -- runs `com.mjdowney.rich-comment-tests/run-ns-tests!` on the current namespace to treat any `comment` forms annotated as `:rct/test` as actual tests; see https://github.com/matthewdowney/rich-comment-tests/ for more details.
* `ctrl+alt+space t` -- run all the tests in the current namespace (use `ctrl+alt+space o` to see output/report) and display a summary of results.
* `ctrl+alt+space x` -- run all the tests in the "associated" namespace (use `ctrl+alt+space o` to see output/report) and display a summary of results; if the current namespace is `foo.bar`, this will look for `foo.bar-test` or `foo.bar-expectations`.
* `ctrl+alt+space z` -- zap (remove) the current namespace's definitions: occasionally useful for cleaning up REPL state; this unaliases/unmaps all the symbols in the namespace _without destroying the namespace itself_, leaving it "empty" so you can load the file from disk again so it is fully-sync'd (`ctrl+alt+c enter`).

Several of these command snippets _require_ Clojure 1.10 or later (because they assume `requiring-resolve` and `tap>`) and will fail on earlier versions. Portal supports Clojure 1.10's `datafy` and `nav`.

## Joyride Scripts

The following scripts perform tasks that my old Clover configuration used to provide. You'll need Joyride installed in VS Code and you'll need at least v2.0.277 of Calva since these scripts depend on the recently-added [Calva Extension API](https://calva.io/api/).

* `clojuredocs.cljs` -- with a Clojure symbol selected, this will open VS Code's Simple Browser, directly inside VS Code, at the corresponding [ClojureDocs page](https://clojuredocs.org).
* `javadoc.cljs` -- with a Java class name selected, or a Clojure expression selected, this will open VS Code's Simple Browser, directly inside VS Code, at the corresponding (Oracle) JavaDoc page for the class that the selection resolves to. If the underlying `javadoc-url` function doesn't recognize the class, it produces a Google "I feel lucky" URL that will be opened in an _external_ browser instead (since Simple Browser cannot open Google's site).
* `ns.cljs` -- without moving the cursor, evaluate the current file's `ns` form.

> Note: these scripts assume you have a Clojure nREPL connected in Calva to perform symbol/class resolution!

My `keybindings.json` file has key bindings for these that are intended to match (but override) what would be custom REPL command snippet key bindings, to make these Joyride scripts feel more like snippets:

* `ctrl+alt+space j` -- runs `javadoc.cljs` as a User-level Joyride script.
* `ctrl+alt+space /` -- runs `clojuredocs.cljs` as a User-level Joyride script (in my Clover setup, this used to be `ctrl+; shift+/`, i,e., `?`, but `shift+/` would be a bit unwieldy as a `ctrl+alt+space` chord).

In addition, `ctrl+alt+n ctrl+alt+s` will run `ns.cljs`.

## Keymap

Other key bindings that you may or may not find useful:

* `ctrl+alt+e ctrl+alt+j` -- join window groups in VS Code (I often use this after starting the REPL or splitting a window).
* `ctrl+alt+e ctrl+alt+right` -- move the current window to the next group to the right in VS Code (I often use this after starting the REPL or splitting a window).
* `ctrl+t ctrl+f` -- switch focus to the terminal window in VS Code.
* `ctrl+t ctrl+t` -- open a terminal window in VS Code.
* `ctrl+alt+l ctrl+alt+l` -- convert the selection/word at the cursor to lowercase.
* `ctrl+alt+u ctrl+alt+u` -- convert the selection/word at the cursor to uppercase.
* `ctrl+alt+k ctrl+alt+t` -- toggle between Clojure implementation and test files.

* `alt+home` -- `paredit.openList` for Calva to avoid `ctrl+home` (which should be `cursorTop`!).
* `ctrl+j` -- join lines (this is the default on macOS but not Windows).

If you use Atlassian/Jira/BitBucket:

* `ctrl+alt+a ctrl+alt+b` -- switch to BitBucket PRs.
* `ctrl+alt+a ctrl+alt+j` -- switch to Jira issues.
