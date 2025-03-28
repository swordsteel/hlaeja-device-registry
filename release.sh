#!/bin/sh

### This should be a pipeline, but for this example let use this ###

check_active_branch() {
  if [ "$(git rev-parse --abbrev-ref HEAD)" != "$1" ]; then
    echo "Error: The current branch is not $1."
    exit 1
  fi
}

check_uncommitted_changes() {
  if [ -n "$(git status --porcelain)" ]; then
    echo "Error: There are uncommitted changes in the repository."
    exit 1
  fi
}

prepare_environment() {
    git fetch origin
}

check_last_commit() {
  last_commit_message=$(git log -1 --pretty=format:%s)
  if [ "$last_commit_message" = "[RELEASE] - bump version" ]; then
    echo "Warning: Nothing to release!!!"
    exit 1
  fi
}

check_differences() {
  if ! git diff --quiet origin/"$1" "$1"; then
    echo "Error: The branches origin/$1 and $1 have differences."
    exit 1
  fi
}

un_snapshot_version() {
  sed -i "s/\($1\s*=\s*[0-9]\+\.[0-9]\+\.[0-9]\+\).*/\1/" gradle.properties
}

current_version() {
    awk -F '=' '/version\s*=\s*[0-9.]*/ {gsub(/^ +| +$/,"",$2); print $2}' gradle.properties
}

stage_files() {
    for file in "$@"; do
        if git diff --exit-code --quiet -- "$file"; then
            echo "No changes in $file"
        else
            git add "$file"
            echo "Changes in $file staged for commit"
        fi
    done
}

commit_change() {
    stage_files gradle.properties
    git commit -m "[RELEASE] - $1"
    git push --porcelain origin master
}

add_release_tag() {
  gitTag="v$(current_version)"
  git tag -a "$gitTag" -m "Release version $gitTag"
  git push --porcelain origin "$gitTag"
}

snapshot_version() {
  new_version="$(current_version | awk -F '.' '{print $1 "." $2+1 ".0"}')"
  sed -i "s/\(version\s*=\s*\)[0-9.]*/\1$new_version-SNAPSHOT/" gradle.properties
}

handle_sql_files() {
  version=$(current_version)
  sql_dir="sql"
  version_dir="${sql_dir}/v${version}"
  if [ -d "$sql_dir" ] && [ -n "$(ls -A $sql_dir/*.sql 2>/dev/null)" ]; then
    mkdir -p "$version_dir"
    mv "$sql_dir"/*.sql "$version_dir/"
    git add "$sql_dir"
  fi
}

# check and prepare for release
check_active_branch master
check_uncommitted_changes
prepare_environment
check_last_commit
check_differences master

# un-snapshot version for release
un_snapshot_version version
un_snapshot_version catalog

# release changes and prepare for next release
commit_change "release version: $(current_version)"
add_release_tag
handle_sql_files
snapshot_version
commit_change 'bump version'
