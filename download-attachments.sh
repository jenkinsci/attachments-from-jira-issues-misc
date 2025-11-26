#!/usr/bin/env bash

: "${JIRA_MIGRATION_JIRA_URL:? Missing Jira base URL (e.g., https://issues.jenkins.io)}"

input_file="all_attachments.txt"
mapping_folder="mappings"
mapping_file="${mapping_folder}/jira_attachments_repo_id_filename.txt"
current_repo="$(gh repo view --json name,owner --template '{{.owner.login}}/{{.name}}')"

base_url="${JIRA_MIGRATION_JIRA_URL}/secure/attachment"

urlencode() {
    # URL-encode a string
    local LANG=C
    local length="${#1}"
    for (( i = 0; i < length; i++ )); do
        local c="${1:i:1}"
        case $c in
            [a-zA-Z0-9.~_-]) printf "%s" "${c}" ;;
            *) printf '%%%02X' "'${c}" ;;
        esac
    done
}

if [[ ! -f "${input_file}" ]];then
    echo "You have to create and copy ${input_file} in this folder first."
    echo "This file is expected to contains a colon-separated list of attachment ids and their filenames."
    exit 1
fi

mkdir -p "${mapping_folder}"
touch "${mapping_file}"

# Count non-empty lines
total=$(grep -vc '^$' "${input_file}")
count=0

while IFS=: read -r id filename; do
    count=$((count+1))
    pct=$(printf "%.2f" "$(echo "${count}*100/${total}" | bc -l)")
    # skip empty lines
    [ -z "${id}" ] && continue

    # download target path
    target="attachments/${id}/${filename}"

    if [ -f "${target}" ]; then
        echo "[${count}/${total} | ${pct}%] Skipping $id (already downloaded)"
        continue
    fi

    # create directory
    mkdir -p "attachments/${id}"

    encoded_filename=$(urlencode "$filename")
    url="${base_url}/${id}/${encoded_filename}"


    echo "[${count}/${total} | ${pct}%] Downloading ${id} to ${target}"

    # download the file
    curl -s -L "${url}" -o "${target}"

    # Mapping
    echo "${id}:${current_repo}/refs/heads/main/${target}" >> "${mapping_file}"
done < "${input_file}"

echo "You can now update https://github.com/jenkinsci/artifacts-from-jira-issues/blob/main/mappings/jira_attachments_repo_id_filename.txt with ${mapping_file} content"
echo "(ensure there is no attachment id duplicate)"
