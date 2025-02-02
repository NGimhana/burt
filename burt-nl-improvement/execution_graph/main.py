import concurrent
import math
import traceback

from os import listdir
import json
import logging
from pathlib import Path
import csv
import spacy
from spacy.symbols import *
import concurrent.futures

import multiprocessing
import os

# os.chdir("../../burt-nl-improvement")

from subprocess import PIPE, run
from spacy.lang.en.stop_words import STOP_WORDS

def split_in_folds(list, n):
    """Yield successive n-sized chunks from list."""
    for i in range(0, len(list), n):
        yield list[i:i + n]



def write_json_line_by_line(data, file_path):
    with open(file_path, 'w') as dest_file:
        for record in data:
            print(json.dumps(record), file=dest_file)


def parse_dXml(idXml):
    if "NO_ID" in idXml or idXml == "" or "/" not in idXml:
        return None

    else:
        return idXml.split("/")[1]


def parse_type(type):
    if type:
        key_word = type.split(".")[-1]
        # print(key_word)
        view_group = ["viewgroup", "layout", "viewpager", "tabhost", "tabwidget", "tablerow", "viewpager"]
        if key_word.lower() == "view" or any(s in key_word.lower() for s in view_group):
            return False
        else:
            return True
    else:
        return False


def read_json(file, data_source):
    result = []

    with open(file) as json_file:
        execution_dict = json.load(json_file)
        steps = execution_dict["steps"]

        for i in range(len(steps)):
            step_contents = {}

            step = steps[i]
            action_code = step["action"]
            if action_code == 99:
                continue

            step_contents["stepID"] = step["sequenceStep"]
            step_contents["dataSource"] = data_source

            if "screenshot" not in step:
                print("the", step["sequenceStep"], "step does not have screenshot")
            else:
                screenshot_path = step["screenshot"]

            step_contents["screenshotPath"] = screenshot_path
            step_contents["action"] = {}

            if "dynGuiComponent" in step:

                dyn_gui_component = step["dynGuiComponent"]

                if "name" in dyn_gui_component:
                    type = dyn_gui_component["name"]
                    if parse_type(type):
                        step_contents["action"]["type"] = type.split(".")[-1]

                        if "text" in dyn_gui_component:
                            if dyn_gui_component["text"]:
                                step_contents["action"]["text"] = dyn_gui_component["text"]

                        if "contentDescription" in dyn_gui_component:
                            if dyn_gui_component["contentDescription"]:
                                step_contents["action"]["content_description"] = dyn_gui_component["contentDescription"]

                        if "idXml" in dyn_gui_component:
                            idXml = dyn_gui_component["idXml"]
                            xml_result = parse_dXml(idXml)
                            if xml_result:
                                step_contents["action"]["idXml"] = xml_result

                            # ----------------------------
            screen = step["screen"]
            components_list = []
            for gui_component in screen["dynGuiComponents"]:
                content_screen = {}

                if "name" in gui_component:
                    type = gui_component["name"]
                    if parse_type(type):
                        content_screen["type"] = type.split(".")[-1]

                        if "text" in gui_component:
                            if gui_component["text"]:
                                content_screen["text"] = gui_component["text"]

                        if "contentDescription" in gui_component:
                            if gui_component["contentDescription"]:
                                content_screen["content_description"] = gui_component["contentDescription"]

                        if "idXml" in gui_component:
                            each_idXml = gui_component["idXml"]
                            xml_result = parse_dXml(each_idXml)
                            if xml_result is not None:
                                content_screen["idXml"] = xml_result

                if len(content_screen) > 0:
                    components_list.append(content_screen)

            step_contents["screen"] = components_list

            result.append(step_contents)

        return result


def write_csv_from_json_set(result, tokens_csv_output_file_path):
    with open(tokens_csv_output_file_path, "w", newline='') as csvfile:
        writer = csv.writer(csvfile)
        writer.writerow(["words and phrases"])
        for each_word in result:
            writer.writerow([each_word])



def write_csv_from_json_list(result, exec_output_file_path):
    with open(exec_output_file_path, "w", newline='') as csvfile:
        writer = csv.writer(csvfile)
        writer.writerow(["dataSource", "stepID", "screenshotPath", "text", "contentDescription", "idXml", "type"])

        for each_json in result:
            data_source = each_json["dataSource"]
            stepID = each_json["stepID"]
            screenshotPath = each_json["screenshotPath"]
            action = each_json["action"]
            # print(data_source)
            # print(screenshotPath)
            # print(stepID)

            components = each_json["screen"]
            for component in components:
                if "text" in component:
                    text_cp = component["text"]
                else:
                    text_cp = ""

                if "contentDescription" in component:
                    if component["contentDescription"]:
                        content_cp = component["contentDescription"]
                else:
                    content_cp = ""

                if "idXml" in component:
                    idXml_cp = component["idXml"]
                else:
                    idXml_cp = ""

                if "type" in component:
                    type_cp = component["type"]
                else:
                    type_cp = ""
                writer.writerow([data_source, stepID, screenshotPath, text_cp, content_cp, idXml_cp, type_cp])


def get_single_nouns(text, tokens):
    nlp = spacy.load("en_core_web_sm")
    doc = nlp(text)
    for token in doc:
        if token.pos in [NOUN, PROPN]:
            tokens.add(token.lower_)


def get_noun_phrases_verbs(text, tokens):
    nlp = spacy.load("en_core_web_sm")

    # Merge noun phrases and entities for easier analysis
    nlp.add_pipe("merge_noun_chunks")
    doc = nlp(text)
    # remove stopwords

    if len(doc) < 10:
        root = [token for token in doc if token.head == token][0]
        if root.pos == VERB:
            subj = [w for w in root.lefts if w.dep_ == "nsubj"]
            for sub in subj:
                tokens.add(sub.text.lower())
            dobj = [w for w in root.rights if w.dep_ == "dobj"]
            for obj in dobj:
                tokens.add(obj.text.lower())

            tokens.add(root.lemma_)
        elif root.pos == NOUN:
            tokens.add(root.text.lower())

        #     if token.pos in [PROPN, NOUN]:
        #         tokens.add(token.text.lower())
        #     if token.pos == VERB:
        #         tokens.add(token.lemma_)
        #
        # for chunk in new_doc.noun_chunks:
        #
        #     tokens.add(chunk.text.lower())
        #     if chunk.root.dep in [dobj, nsubj] and chunk.root.head.pos == VERB:
        #         print("verb: ", chunk.root.head.text)
        #         tokens.add(chunk.root.head.text)


def remove_stop_words(text):
    nlp = spacy.load("en_core_web_sm")
    doc = nlp(text)
    token_list = []
    for token in doc:
        token_list.append(token.text)
    filtered_text_list = []
    for word in token_list:

        lexeme = nlp.vocab[word]
        if not lexeme.is_stop:
            filtered_text_list.append(word)

    if len(filtered_text_list) > 0:
        filtered_text = " ".join(filtered_text_list)
        return filtered_text.replace("_", " ").replace("-", " ").replace("¦", "").lower()


def extract_phrases(json_list):
    tokens = set()
    for each_json in json_list:

        data_source = each_json["dataSource"]
        stepID = each_json["stepID"]
        screenshotPath = each_json["screenshotPath"]
        action = each_json["action"]
        #print(data_source)
        #print(screenshotPath)
        #print(stepID)
        components = each_json["screen"]

        for component in components:
            type_cp = component["type"]
            if type_cp.lower() != "edittext":
                if "text" in component:
                    text_cp = component["text"]
                    new_text = remove_stop_words(text_cp)
                    if new_text:
                        get_single_nouns(new_text, tokens)
                        get_noun_phrases_verbs(new_text, tokens)

            if "contentDescription" in component:
                content_cp = component["contentDescription"]
                if content_cp:
                    new_content_cp = remove_stop_words(content_cp)
                    if new_content_cp:
                        get_noun_phrases_verbs(new_content_cp, tokens)
                        get_single_nouns(new_content_cp, tokens)

            if "idXml" in component:
                idXml_cp = component["idXml"]
                if idXml_cp:
                    new_idXml_cp = remove_stop_words(idXml_cp)
                    if new_idXml_cp:
                        get_noun_phrases_verbs(new_idXml_cp, tokens)
                        get_single_nouns(new_idXml_cp, tokens)
        # print(tokens)

    return tokens


if __name__ == '__main__':

    app_version_packages = {
        "ATimeTracker": "com.markuspage.android.atimetracker",
        "GnuCash": "org.gnucash.android",
        "mileage": "com.evancharlton.mileage",
        "droidweight": "de.delusions.measure",
        "AntennaPod": "de.danoeh.antennapod.debug",
        "growtracker": "me.anon.grow",
        "androidtoken": "uk.co.bitethebullet.android.token"
    }

    systems = [
        ("GnuCash", "2.1.3"),
        ("mileage", "3.1.1"),
        ("droidweight", "1.5.4"),
        ("GnuCash", "1.0.3"),
        ("AntennaPod", "1.6.2.3"),
        ("ATimeTracker", "0.20"),
        ("growtracker", "2.3.1"),
        ("androidtoken", "2.10"),

    ]

    data_sources = ["CrashScope", "TraceReplayer"]
    json_list = []
    output_folder = "extracted_data"
    csv_output_file_path = "extracted_information.csv"
    tokens_csv_output_file_path = "extracted_tokens.csv"


    num_workers = multiprocessing.cpu_count()

    for system in systems:
        system_name, system_version = system

        # read execution file
        data_folder = "../data"

        for data_source in data_sources:

            data_source_folder = data_source + "-Data"
            package_name = app_version_packages[system_name]

            data_location = os.path.join(data_folder, data_source_folder, package_name + "-" + system_version)

            onlyExecutionfiles = [f for f in listdir(data_location) if "Execution-" in f]

            for file in onlyExecutionfiles:

                try:
                    execution_file_path = os.path.join(data_location, file)
                    result = read_json(execution_file_path, data_source + "-" + file)

                    json_list.extend(result)

                    Path(os.path.join(output_folder, package_name + "-" + system_version)).mkdir(parents=True,
                                                                                                 exist_ok=True)
                    exec_output_file_path = os.path.join(output_folder, package_name + "-" + system_version,
                                                         data_source + "-" + file)

                    write_json_line_by_line(result, exec_output_file_path)

                except UnicodeError as e:
                    logging.error('Failed to read execution file: ' + str(e))

    write_csv_from_json_list(json_list, os.path.join(output_folder, csv_output_file_path))

    fold_size = math.ceil(len(json_list) / (num_workers))
    json_folds = list(split_in_folds(json_list, fold_size))
    print(len(json_folds))
    final_result = set()
    with concurrent.futures.ProcessPoolExecutor(max_workers=num_workers) as executor:
        futures = []
        try:
            for i in range(0, len(json_folds)):
                print(i)
                # json_folds = list(split_in_folds(json_list, fold_size))
                # print(len(json_folds))
                futures.append(
                    executor.submit(extract_phrases, json_folds[i]))

            for future in concurrent.futures.as_completed(futures):
                final_result.update(list(future.result()))

        except Exception as e:
            print(e)
            traceback.print_exc()
    write_csv_from_json_set(final_result, tokens_csv_output_file_path)


