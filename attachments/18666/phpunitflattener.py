#!/usr/bin/env python
from xml.etree import ElementTree as ET

def main(filepath):
    # Create an XML tree from the file to parse.
    xml = open(filepath).read()
    tree = ET.fromstring(xml)
    # Create the new root which will store leaf nodes.
    flattenedRoot = ET.XML("<testsuites/>")
    # Iterate over all the testsuites.
    for testsuite in tree.findall(".//testsuite"):
        # If this is a leaf testsuite, add this to the new root.
        if testsuite.get("file"):
            flattenedRoot.append(testsuite)
    # Now write back out the new flatted tree!
    open("phpunit-flattened.xml", "w").write(ET.tostring(flattenedRoot))

if __name__ == "__main__":
    import sys
    main(sys.argv[1])

