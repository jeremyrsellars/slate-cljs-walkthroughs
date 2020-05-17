# Publishing to CLJSJS

After building with `./externs.sh` replace the externs files `*.ext.js` in the `slate` and `slate-react` directories of your clone of https://github.com/cljsjs/packages.

### Install locally

To install locally for test: https://github.com/cljsjs/packages/wiki/Creating-Packages#7-install-your-package-locally-and-try-it-out

    cd path/to/cljsjs/packages/slate
    boot package install target
    cd path/to/cljsjs/packages/slate-react
    boot package install target

### Uninstalling local maven package

In a directory containing a fake [POM](./resources/pom.xml) file that references the packages you wish to uninstall, run Maven like this: 

    mvn dependency:purge-local-repository
