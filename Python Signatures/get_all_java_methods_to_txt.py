import codecs
import re
import sys
import cv2
import os.path
from bs4 import BeautifulSoup

def load_html_file(dir):
    f = codecs.open(dir, 'r')
    html = f.read()
    soup = BeautifulSoup(html, 'html.parser')
    f.close()
    return soup

def get_links_list(soup):
    href_list = []
    for link in soup.findAll('a'):
        tmp_href = link.get('href')
        href_list.append(tmp_href)
    return href_list


ROOT_DIR = sys.argv[1]
JAVA_DIR = ROOT_DIR + "../../javadoc/"

ADD_JAVA = False
if os.path.isfile(JAVA_DIR + "allclasses-noframe.html"):
    ADD_JAVA = True

soup = load_html_file(JAVA_DIR + "allclasses-noframe.html")
href_list = get_links_list(soup)
# open all the classes
for class_link in href_list:
    soup = load_html_file(JAVA_DIR + class_link)
    prev_signature = ""
    method_name = ""
    signature_has_changed = False
    method_detail = soup.find("a", {"name": "method.detail"})
    if method_detail != None:
        for method in method_detail.parent.findAll("pre"):
            signature = ' '.join(method.text.split())
            #print signature
            tmp_name = signature.rpartition("(")[0]
            if method_name != tmp_name:
                method_name = tmp_name
                if prev_signature != "":
                    signature_has_changed = True

            if len(prev_signature) > len(signature):
                tmp_signature = signature
                signature = prev_signature
                prev_signature = tmp_signature

            # check if signature is repeated
            # example "create(" and "create(int x)"
            if prev_signature[:-1] in signature:
                if prev_signature != "":
                    signature = prev_signature[:-1] + "[" + signature[len(prev_signature) - 1:-1] + "])"
            else:
                signature_has_changed = True

            if signature_has_changed:
                signature_has_changed = False
                print signature

            prev_signature = signature

        print signature
