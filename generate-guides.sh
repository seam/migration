#!/bin/bash

# Require BASH 3 or newer

REQUIRED_BASH_VERSION=3.0.0

if [[ $BASH_VERSION < $REQUIRED_BASH_VERSION ]]; then
  echo "You must use Bash version 3 or newer to run this script"
  exit
fi

# Canonicalise the source dir, allow this script to be called anywhere

SOURCE="${BASH_SOURCE[0]}"
while [ -h "$SOURCE" ] ; do SOURCE="$(readlink "$SOURCE")"; done
DIR="$( cd -P "$( dirname "$SOURCE" )" && pwd )"

# DEFINE

TARGET=$DIR/target/guide

echo "** Building tutorial"

echo "**** Cleaning $TARGET"
rm -rf $TARGET
mkdir -p $TARGET

echo "**** Copying shared resources to $TARGET"
cp -r gfx $TARGET

files=`find * -iname \*.asciidoc`
for file in $files
do
    output_filename_html=$TARGET/${file//.asciidoc/.html}
    output_filename_docbook=$TARGET/${file//.asciidoc/.pdf}
    output_filename_epub=$TARGET/${file//.asciidoc/.epub}
    echo "**** Processing $file > ${output_filename_html}"
    asciidoc -b html5 -a toc2 -a pygments -o ${output_filename_html} $file  
    echo "**** Processing $file > ${output_filename_docbook}"
    a2x --dblatex-opts "-P latex.output.revhistory=0" -D $TARGET $file
    echo "**** Processing $file > ${output_filename_epub}"
    a2x -f epub -D $TARGET $file
done
