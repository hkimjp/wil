FROM hkim0331/clj-cljs

ENV DEBIAN_FRONTEND=noninteractive

# Add apt-utils sudo git
RUN apt-get update \
    && apt-get -y install --no-install-recommends \
       npm 2>&1

# Clean up
RUN apt-get autoremove -y \
    && apt-get clean -y \
    && rm -rf /var/lib/apt/lists/*

ENV DEBIAN_FRONTEND=dialog

