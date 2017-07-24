# TODO: clarify when there are several C++ signatures and only one Python one for the same function.
#       i.e. calcHist() - http://docs.opencv.org/trunk/d6/dc7/group__imgproc__hist.html#ga4b2b5fd75503ff9e6844cc4dcdaed35d
#       special case - http://docs.opencv.org/trunk/db/de0/group__core__utils.html#ga4910d7f86336cd4eff9dd05575667e41
import codecs
import re
import sys
import cv2
from bs4 import BeautifulSoup


def load_html_file(dir):
    f = codecs.open(dir, 'r')
    html = f.read()
    soup = BeautifulSoup(html, 'html.parser')
    return soup


def is_module_link(link):
    if link is None:
        return False
    if "group" not in link:
        return False
    if "#" in link:
        return False
    return True


def get_links_list(soup):
    href_list = []
    for link in soup.findAll('a'):
        tmp_href = link.get('href')
        if is_module_link(tmp_href):
            href_list.append(tmp_href)
    return href_list


def add_item(new_row, color, text):
    new_item = soup.new_tag('td')
    if color == "red":
        new_item = soup.new_tag('td', **{'class': 'paramname'})
    new_item.append(text)
    new_row.append(new_item)
    return new_row


def add_signature_to_table(new_row, signature, function_name):
    if "->" in signature:
        new_item = soup.new_tag('td')
        new_item.append(signature.split("->", 1)[1] + ' =')
        new_row.append(new_item)
    new_row = add_item(new_row, "black", 'cv2.' + function_name + '(')
    new_row = add_item(new_row, "red", signature.partition('(')[-1].rpartition(')')[0])
    new_row = add_item(new_row, "black", ')')
    return new_row


def new_line(python_table, new_row):
    python_table.append(new_row)
    new_row = soup.new_tag('tr')
    return new_row


def add_bolded(new_row, text):
    new_item = soup.new_tag('th')
    new_item.append(text)
    new_row.append(new_item)
    return new_row


def add_python_signatures(soup, tmp_dir):
    #print tmp_dir
    for function in soup.findAll("h2", {"class": "memtitle"}):
        function_name = function.getText()

        if "()" not in function_name:
            continue
        else:
            if "[" in function_name:
                if "[1/" in function_name:
                    function_name = function_name.replace(' ', '')[2:-7]
                else:
                    continue
            else:
                function_name = function_name.replace(' ', '')[2:-2]

            try:
                method = getattr(cv2, str(function_name))
            except:
                continue

            signature = str(method.__doc__)
            if signature != "":
                cpp_table = function.findNext('table')
                python_table = soup.new_tag('table')
                new_row = soup.new_tag('tr')
                new_row = add_bolded(new_row, 'Python:')

                if len(signature) > 120:
                    new_row = new_line(python_table, new_row)

                if " or " in signature:
                    for tmp_sig in signature.split(" or "):
                        new_row = new_line(python_table, new_row)
                        new_row = add_signature_to_table(new_row, tmp_sig, function_name)
                        new_row = new_line(python_table, new_row)
                else:
                    new_row = add_signature_to_table(new_row, signature, function_name)
                    python_table.append(new_row)

                # insert the new table after the current table
                cpp_table.insert_after(python_table)
    with open(tmp_dir, "w") as file:
        file.write(str(soup))
        file.close()
    pass


root_dir = sys.argv[1]
soup = load_html_file(root_dir + "index.html")
href_list = get_links_list(soup)

for link in href_list:
    # add python singatures to the module
    soup = load_html_file(root_dir + link)
    sub_href_list = get_links_list(soup)
    add_python_signatures(soup, root_dir + link)

    # add python sigantures to the sub-modules
    link = re.sub(r"group__.+html", "", link)
    for sub_link in sub_href_list:
        tmp_dir = root_dir + link + sub_link
        soup = load_html_file(tmp_dir)
        add_python_signatures(soup, tmp_dir)
