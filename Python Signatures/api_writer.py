import codecs
import re
import sys
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


def remove_square_brackets(str):
    #smaller_sig = smaller_sig.translate(None, '[]')
    return re.sub('\[*\]*', '', str)


def match(method_name, smaller_sig, larger_sig):
    # remove [] from signatures
    smaller_sig = remove_square_brackets(method_name + smaller_sig)
    larger_sig = remove_square_brackets(method_name + larger_sig)
    return smaller_sig in larger_sig


def get_text_between_chars(sig, begin_char, end_char):
    return  sig.partition(begin_char)[-1].rpartition(end_char)[0]


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
    else:
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

ROOT_DIR = sys.argv[1]
JAVA_DIR = ROOT_DIR + "../../javadoc/"

if os.path.isfile(JAVA_DIR + "allclasses-noframe.html"):
    ADD_JAVA = True

if ADD_JAVA:
    soup = load_html_file(JAVA_DIR + "allclasses-noframe.html")
    href_list = get_links_list(soup)
    text_file = open("JavaSignatures.txt", "w")

    for class_link in href_list:
        soup = load_html_file(JAVA_DIR + class_link)
        method_detail = soup.find("a", {"name": "method.detail"})
        if method_detail != None:


            methods_html_tag = method_detail.parent
            list = methods_html_tag.findAll("pre")

            # initialize with first element of the loop
            prev_signature = ' '.join(list[0].text.split())
            prev_method_name = prev_signature.rpartition("(")[0]
            name_changed = False
            iter_links = iter(list)
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
                    args_changed, signature = compact_arguments(method_name, prev_signature, signature)

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
