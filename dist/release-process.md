Releasing
=========

1. Ensure you have dblatex, asciidoc and pygments set up and ready to run. This is used to process the markdown files and add syntax highlighting.
2. Make sure you have Bash 3 or newer
3. Make sure you have credentials set up to deploy to `jboss-releases-repository` in your `settings.xml`, and have access to rsync files to `filemgmt.jboss.org/download_htdocs/seam`
4. Release

        dist/release.sh -s <old snapshot version> -r <release version>

