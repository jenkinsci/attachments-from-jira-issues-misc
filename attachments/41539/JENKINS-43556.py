#!/usr/bin/env python
"""Find jobs where the job status doesn't match the wfapi status"""

from __future__ import print_function

import argparse
import requests

def get(*args, **kwargs):
    response = session.get(*args, **kwargs)
    response.raise_for_status()
    return response

def get_json_from_url(url):
    response = get(url)
    build = response.json()
    return build

def build_shows_bug(build_url):
    try:
        build = get_json_from_url(build_url + 'api/json')
        wfapi_build = get_json_from_url(build_url + 'wfapi')
        if build['result'] == 'SUCCESS' and wfapi_build['status'] == 'FAILED':
            return True
    except requests.exceptions.HTTPError as e:
        print(e)
    return False

if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--url', required=True, help="Jenkins URL of a view")
    parser.add_argument('--count', default=100, type=int, help="Number of builds to get")
    args = parser.parse_args()

    url = args.url
    if not url.endswith('/'):
        url += '/'

    session = requests.Session()

    # List of failed URLs
    failed = list()

    # Get all of the jobs in a view
    view = get_json_from_url(url + 'api/json')
    job_urls = [j['url'] for j in view['jobs']]

    # Iterate through jobs
    for job_url in job_urls:
        print("Checking: {}".format(job_url))

        # Get the latest build of a job
        job = get_json_from_url(job_url + 'api/json')
        if job['_class'] != "org.jenkinsci.plugins.workflow.job.WorkflowJob":
            print("{} is not a workflow job".format(job_url))
            continue

        lastBuild = job['lastBuild']['number']
        firstBuild = max(1, lastBuild + 1 - args.count)
        print("Searching builds in range ({},{})".format(firstBuild, lastBuild))

        # Iterate through builds
        for b in range(firstBuild, lastBuild + 1):
            build_url = job_url + str(b) + '/'
            if build_shows_bug(build_url):
                print("Build #{} shows JENKINS-43556".format(b))
                failed.append(build_url)
    print("Found the following builds which show this problem: {}".format(failed))
