import codecs
import pydoc
import re
import sys
import imp

from bs4 import BeautifulSoup


# TODO: clarify when there are several C++ signatures and only one Python one for the same function.
# i.e. calcHist() - http://docs.opencv.org/trunk/d6/dc7/group__imgproc__hist.html#ga4b2b5fd75503ff9e6844cc4dcdaed35d


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


def save_output_of_help_to_file(file_path, request):
    with open(file_path, "w") as f:
        sys.stdout = f
        pydoc.help(request)
        f.close()
        sys.stdout = sys.__stdout__
    pass


def get_signature_to_string(file_path, function_name):
    with open(file_path, 'r') as fin:
        for i, line in enumerate(fin):
            if function_name + "(" and "," in line:
                return line.rstrip()
    return ""


def add_item(new_row, color, text):
    new_item = soup.new_tag('td')
    if color == "red":
        new_item = soup.new_tag('td', **{'class': 'paramname'})
    new_item.append(text)
    new_row.append(new_item)
    return new_row


def add_signature_to_table(new_row, signature):
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


try:
    imp.find_module('cv2')
except ImportError:
    print 'You have not installed the cv2 module'
    sys.exit(-1)

root_dir = sys.argv[1]

# use index.html to get modules
soup = load_html_file(root_dir + "index.html")
href_list = get_links_list(soup)

for link in href_list:
    if "group__core" in link:

        # get the sub-modules
        soup = load_html_file(root_dir + link)
        sub_href_list = get_links_list(soup)

        # get the right link to a sub-module html file
        link = re.sub(r"group__.+html", "", link)
        for sub_link in sub_href_list:
            tmp_dir = root_dir + link + sub_link
            soup = load_html_file(tmp_dir)
            print root_dir + link + sub_link
            for function in soup.findAll("h2", {"class": "memtitle"}):

                # get function_name from html element
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

                    save_output_of_help_to_file(r'tmp_file.txt', str("cv2." + function_name + ""))
                    signature = get_signature_to_string('tmp_file.txt', function_name)

                    if signature != "":
                        print signature

                        cpp_table = function.findNext('table')
                        python_table = soup.new_tag('table')
                        new_row = soup.new_tag('tr')
                        new_row = add_bolded(new_row, 'Python:')

                        if len(signature) > 120:
                            new_row = new_line(python_table, new_row)

                        if " or " in signature:
                            for tmp_sig in signature.split(" or "):
                                new_row = new_line(python_table, new_row)
                                new_row = add_signature_to_table(new_row, tmp_sig)
                                new_row = new_line(python_table, new_row)
                        else:
                            new_row = add_signature_to_table(new_row, signature)
                            python_table.append(new_row)

                        # insert the new table after the current table
                        cpp_table.insert_after(python_table)

            with open(tmp_dir, "w") as file:
                file.write(str(soup))
                file.close()
