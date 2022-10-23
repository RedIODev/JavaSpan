#!/usr/bin/env bash
rm -r outDir
javac -d outDir --release 19 --enable-preview  --module-source-path src $(find src -name "*.java")
