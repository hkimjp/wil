#!/bin/sh
#
# origin: ${utils}/src/bump-version.sh
#
# bump-vesion script for clojure projects.
# confused using macos's /usr/bin/sed. so gsed.
#
# CAUSION
# The POSIX standard does not support back-references for
# "extended" regular expressions,
# this is a compatible extension to that standard.

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

# CHANGELOG.md
VER=$1
TODAY=`date +%F`
${SED} -i -e "/SNAPSHOT/c\
## ${VER} / ${TODAY}" CHANGELOG.md
