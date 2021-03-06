import React from "react";
// Config starter code
import { createChatBotMessage } from "react-chatbot-kit";
import SelectOneScreen from "./components/ScreenOptions/SelectOneScreenOption";
import SelectMultipleScreens from "./components/ScreenOptions/SelectMultipleScreensOption";
import LinkList from "./components/LinkList/LinkList";
import './config.css';
const config = {
    serverEndpoint: "http://localhost:8081",
    saveMessagesService: "/saveMessages",
    loadMessagesService: "/loadMessages",
    initialMessages: [
        createChatBotMessage("Got it. Just to confirm, can you select the screen that is having the problem?",
            {
            widget: "OneScreenOption",
        }
        ),
    ],
    widgets: [
        {
            widgetName: "OneScreenOption",
            widgetFunc: (props) => <SelectOneScreen {...props} />,
        },
        {
            widgetName: "MultipleScreensOptions",
            widgetFunc: (props) => <SelectMultipleScreens {...props} />,
        },
        {
            widgetName: "javascriptLinks",
            widgetFunc: (props) => <LinkList {...props} />,
            props: {
                options: [
                    {
                        text: "Introduction to JS",
                        url:
                            "https://www.freecodecamp.org/learn/javascript-algorithms-and-data-structures/basic-javascript/",
                        id: 1,
                    },
                    {
                        text: "Mozilla JS Guide",
                        url:
                            "https://developer.mozilla.org/en-US/docs/Web/JavaScript/Guide",
                        id: 2,
                    },
                    {
                        text: "Frontend Masters",
                        url: "https://frontendmasters.com",
                        id: 3,
                    },
                ],
            },
        },
    ],
};
export default config