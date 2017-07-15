import codecs
import re
import sys

from BeautifulSoup import BeautifulSoup


def load_html_file(dir):
    f = codecs.open(dir, 'r')
    html = f.read()
    soup = BeautifulSoup(html)

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

root_dir = sys.argv[1]

# use index.html to get modules
soup = load_html_file(root_dir + "index.html")
href_list = get_links_list(soup)

for link in href_list:
    if "group__imgproc" in link:

        # get the sub-modules
        soup = load_html_file(root_dir + link)
        sub_href_list = get_links_list(soup)

        link = re.sub(r"group__.+html","",link)
        for sub_link in sub_href_list:
            if "group__imgproc__filter" in sub_link:
                soup = load_html_file(root_dir + link + sub_link)
                for function in soup.findAll("h2", {"class": "memtitle"}):
                    title = function.getText()
                    title = title.rsplit(';', 1)[1]

                    if "()" not in title:
                        continue
                    else:
                        title = title.replace(' ', '')[:-2]
                        print title
                        help(str("cv2." + title + ""))
