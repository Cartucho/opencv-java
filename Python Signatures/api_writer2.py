"""
This code adds Python and Java signatures to the docs.

TODO:
* clarify when there are several C++ signatures corresponding to a single Python function.
    i.e: calcHist():
    http://docs.opencv.org/3.2.0/d6/dc7/group__imgproc__hist.html#ga4b2b5fd75503ff9e6844cc4dcdaed35d
* clarify special case:
    http://docs.opencv.org/3.2.0/db/de0/group__core__utils.html#ga4910d7f86336cd4eff9dd05575667e41
"""
import codecs
import re
import sys
import os.path
import logging
import cv2

try:
    from bs4 import BeautifulSoup
except ImportError:
    raise ImportError('Error: '
                      'Install BeautifulSoup (bs4) for adding'
                      ' Python & Java signatures documentation')


def load_html_file(file_dir):
    """ Uses BeautifulSoup to load an html """
    tmp_file = codecs.open(file_dir, 'r')
    html = tmp_file.read()
    tmp_soup = BeautifulSoup(html, 'html.parser')
    tmp_file.close()
    return tmp_soup # prettify here?


def is_not_module_link(tmp_link):
    """ Checks if a link belongs to a c++ method """
    if tmp_link is None:
        return True
    if "group" not in tmp_link:
        return True
    if "#" in tmp_link:
        return True
    return False


def get_links_list(tmp_soup, filter_links):
    """ Get a list of links from a soup """
    tmp_href_list = []
    for tmp_link in tmp_soup.findAll('a'):
        tmp_href = tmp_link.get('href')
        if filter_links:
            if is_not_module_link(tmp_href):
                continue
        tmp_href_list.append(tmp_href)
    return tmp_href_list


def add_item(tmp_soup, new_row, is_parameter, text):
    """ Adds a new html tag for the table with the signature """
    new_item = tmp_soup.new_tag('td')
    if is_parameter:
        new_item = tmp_soup.new_tag('td', **{'class': 'paramname'})
    new_item.append(text)
    new_row.append(new_item)
    return new_row


def add_signature_to_table(tmp_soup, new_row, signature, function_name, language):
    """ Add a signature to an html table"""
    if "-> None" in signature:
        pass
    elif "->" in signature:
        new_item = tmp_soup.new_tag('td')
        new_item.append(signature.split("->", 1)[1] + ' =')
        new_row.append(new_item)

    if "Python" in language:
        new_row = add_item(tmp_soup, new_row, False, 'cv2.' + function_name + '(')
    else:
        new_row = add_item(tmp_soup, new_row, False, function_name + '(')

    new_row = add_item(tmp_soup, new_row, True, signature.partition('(')[-1].rpartition(')')[0])
    new_row = add_item(tmp_soup, new_row, False, ')')
    return new_row


def new_line(tmp_soup, python_table, new_row):
    """ Adds a new line to the html table """
    python_table.append(new_row)
    new_row = tmp_soup.new_tag('tr')
    return new_row


def add_bolded(tmp_soup, new_row, text):
    """ Adds bolded text to the table """
    new_item = tmp_soup.new_tag('th')
    new_item.append(text)
    new_row.append(new_item)
    return new_row


def append_table_to(cpp_table, tmp_soup, language, signature, function_name):
    """ Insert the new Python / Java table after the current html c++ table """
    if signature != "":
        python_table = tmp_soup.new_tag('table')
        new_row = tmp_soup.new_tag('tr')
        new_row = add_bolded(tmp_soup, new_row, language)

        if len(signature) > 120:
            new_row = new_line(tmp_soup, python_table, new_row)

        if " or " in signature:
            for tmp_sig in signature.split(" or "):
                new_row = new_line(tmp_soup, python_table, new_row)
                new_row = add_signature_to_table(tmp_soup, new_row, tmp_sig, function_name, language)
                new_row = new_line(tmp_soup, python_table, new_row)
        else:
            new_row = add_signature_to_table(tmp_soup, new_row, signature, function_name, language)
            python_table.append(new_row)

        cpp_table.insert_after(python_table)
    return cpp_table


def add_signatures(tmp_soup, tmp_dir):
    """ Add signatures to the current soup and rewrite the html file"""
    logging.debug(tmp_dir)
    sign_counter = 0
    python_sign_counter = 0

    # the HTML tag & class being used to find functions
    for function in tmp_soup.findAll("h2", {"class": "memtitle"}):
        function_name = function.getText()

        # all functions have () in it's name
        if "()" not in function_name:
            continue

        if "[" in function_name:
            if "[1/" in function_name:
                function_name = function_name.replace(' ', '')[2:-7]
            else:
                continue
        else:
            function_name = function_name.replace(' ', '')[2:-2]
        sign_counter += 1

        cpp_table = function.findNext('table')
        try:
            method = getattr(cv2, str(function_name))
            signature = str(method.__doc__)
            cpp_table = append_table_to(cpp_table, tmp_soup, "Python:", signature, function_name)
            python_sign_counter += 1
        except AttributeError:
            continue

    with open(tmp_dir, "w") as tmp_file:
        tmp_file.write(str(tmp_soup))
        tmp_file.close()
    logging.debug("Added [" + str(python_sign_counter) + \
                  "/" + str(sign_counter) + "] Python signatures")


def remove_square_brackets(tmp_str):
    return re.sub(r'\[*\]*', '', tmp_str)


def match(method_name, smaller_sig, larger_sig):
    # remove [] from signatures
    smaller_sig = remove_square_brackets(method_name + smaller_sig)
    larger_sig = remove_square_brackets(method_name + larger_sig)
    return smaller_sig in larger_sig


def get_text_between_chars(sig, begin_char, end_char):
    return sig.partition(begin_char)[-1].rpartition(end_char)[0]


def join_arguments(smaller_sig, larger_sig):
    # only one of them contains []
    if "[" in smaller_sig:
        """
        Example:
            input: function(int a[, int b]) and function(int a, int b, int c)
            output: function(int a[, int b[, int c]])
        """
        tmp_smaller = remove_square_brackets(smaller_sig)
        diff_string = larger_sig[len(tmp_smaller):]
        common_string = larger_sig[:len(tmp_smaller)]
        print common_string
        pos = tmp_smaller.index(common_string) + len(common_string) + smaller_sig.count('[')
        return smaller_sig[:pos] + '[' + diff_string + "]" + smaller_sig[pos:]
    """
    Example:
        input: function(int a, int b) and function(int a, int b, int c)
        output: function(int a, int b[, int c])
    """
    diff_string = larger_sig[len(smaller_sig):]
    return larger_sig.replace(diff_string, "[" + diff_string + "]")


def compact_arguments(method_name, prev_signature, signature):
    # compact different arguments for the same signature
    sig_arguments = get_text_between_chars(signature, "(", ")")
    prev_sig_arguments = get_text_between_chars(prev_signature, "(", ")")
    args_changed = False

    if match(method_name, sig_arguments, prev_sig_arguments):
        sig_arguments = join_arguments(sig_arguments, prev_sig_arguments)
    elif match(method_name, prev_sig_arguments, sig_arguments):
        sig_arguments = join_arguments(prev_sig_arguments, sig_arguments)
    else:
        args_changed = True

    return args_changed, method_name + "(" + sig_arguments + ")"


def java_signatures_to_file(classes_dir):
    tmp_soup = load_html_file(classes_dir)
    tmp_href_list = get_links_list(tmp_soup, False)
    text_file = open("JavaSignatures.txt", "w")

    for class_link in tmp_href_list:
        tmp_soup = load_html_file(JAVA_DIR + class_link)
        method_detail = tmp_soup.find("a", {"name": "method.detail"})
        if method_detail != None:

            methods_html_tag = method_detail.parent
            methods_list = methods_html_tag.findAll("pre")

            # initialize with first element of the loop
            prev_signature = ' '.join(methods_list[0].text.split())
            prev_method_name = prev_signature.rpartition("(")[0]
            name_changed = False
            iter_links = iter(methods_list)
            next(iter_links)

            for method in iter_links:
                signature = ' '.join(method.text.split())
                # the name comes before the "(" character -> function_name(args)
                method_name = signature.rpartition("(")[0]

                if prev_method_name != method_name:
                    # update method name
                    prev_method_name = method_name
                    name_changed = True
                else:
                    # compact different arguments for the same signature
                    args_changed, signature = compact_arguments(
                        method_name, prev_signature, signature)

                if name_changed or args_changed:
                    # add previous method signature to file
                    if name_changed:
                        text_file.write(prev_signature + "\n")
                        name_changed = False
                    else:
                        text_file.write(prev_signature + " or ")
                        args_changed = False

                prev_signature = signature

            # add the last method of the current class to the file
            text_file.write(prev_signature + "\n")
    # close the file with all the methods
    text_file.close()


ROOT_DIR = sys.argv[1]
JAVA_DIR = ROOT_DIR + "../../javadoc/"

if os.path.isfile(JAVA_DIR + "allclasses-noframe.html"):
    ADD_JAVA = True

if ADD_JAVA:
    java_signatures_to_file(JAVA_DIR + "allclasses-noframe.html")

soup = load_html_file(ROOT_DIR + "index.html")
href_list = get_links_list(soup, True)

for link in href_list:
    # add python signatures to the module
    soup = load_html_file(ROOT_DIR + link)
    sub_href_list = get_links_list(soup, True)
    add_signatures(soup, ROOT_DIR + link)

    # add python signatures to the sub-modules
    link = re.sub(r"group__.+html", "", link)
    for sub_link in sub_href_list:
        tmp_dir = ROOT_DIR + link + sub_link
        soup = load_html_file(tmp_dir)
        add_signatures(soup, tmp_dir)
