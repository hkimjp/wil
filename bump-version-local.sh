#!/bin/sh
#

if [ -z "$1" ]; then
    echo "usage: $0 <version>"
    exit
fi

if [ -x "${HOMEBREW_PREFIX}/bin/gsed" ]; then
    SED="${HOMEBREW_PREFIX}/bin/gsed -E -i"
else
    SED="/usr/bin/sed -E -i"
fi

# project.clj
${SED} -e "s/(defproject \S+) \S+/\1 \"$1\"/" project.clj

# clj
#${SED} -e "s/(def \^:private version) .+/\1 \"$1\")/" src/core.clj

# cljs
now=`date '+%F %T'`
${SED} -e "s/(def \^:private version) .+/\1 \"$1\")/" \
       -e "s/(def \^:private updated) .+/\1 \"$now\")/" src/cljs/wil/core.cljs


