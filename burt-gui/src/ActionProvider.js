// import Chatbot from "react-chatbot-kit";

import {createChatBotMessage} from "react-chatbot-kit";

const axios = require('axios')
// ActionProvider starter code
// import sessions from "./session/Session";
const context = "";
class ActionProvider{

    constructor(createChatBotMessage, setStateFunc, createClientMessage,  sessionIdObj) {
        this.createChatBotMessage = createChatBotMessage;
        this.setState = setStateFunc;
        this.sessionId = sessionIdObj
    }

    saveSingleMessage(messageObj){

        console.log("Saving single message for ", this.sessionId, ": ", messageObj)
        //calling the API
        axios
            .post('http://localhost:8081/saveSingleMessage', {
                sessionId: this.sessionId,
                messages : [messageObj]
            })
            .then(res => {
                console.log(`Done`)
            })
            .catch(error => {
                console.error(error)
            })
    }

    selectApp(){
        const msg = this.createChatBotMessage(
            "Sure. To start, please select the app that is having the problem",
            {
                widget: "OneScreenOption",
            })
        this.updateChatbotState(msg)
    }


    answerSelectedOneScreen(image){
        const msg = this.createChatBotMessage("you selected" + image + ", shall we continue?")
        this.updateChatbotState(msg)
    }

    //should embed the app name in the message
    askForOB(){
        const msg = this.createChatBotMessage("Ok, can you please tell me the incorrect behavior of Chikki that you observed")
        this.updateChatbotState(msg)
    }

    confirmOB(){
        const msg = this.createChatBotMessage(
         " Got it. Just to confirm, can you select the Chikii screen that is having the problem? " + "Please hit the “Done” button when you are done.",
            {
                widget: "OneScreenOption"
            })
        this.updateChatbotState(msg)
    }

    handleSave(){
        const msg = this.createChatBotMessage("I am saving...")
        this.updateChatbotState(msg)
    }

    handleOneScreenOption = () => {
        const message = this.createChatBotMessage(
            "Okay. please choose more than one screen",
            {
                widget: "MultipleScreensOptions",
            }
        );

        this.updateChatbotState(message);
    };
    handleNoneOfAboveScreensOption = () => {
        const message = this.createChatBotMessage(
            "Okay. I got it",

        );

        this.updateChatbotState(message);
    };
    handleMultipleScreensOption = () => {
        const message = this.createChatBotMessage(
            "Okay. you choose more than one screen",

        );

        this.updateChatbotState(message);
    };

    updateChatbotState2(message, values, values2) {
        const messageObj = this.createChatBotMessage(
            message.message,
            {
                widget: message.widget,
            }
        );

        // const messageObj2 = this.createChatBotMessage(
        //     " Got it. Just to confirm, can you select the Chikii screen that is having the problem? " + "Please hit the “Done” button when you are done.",
        //     {
        //         widget: "OneScreenOption"
        //     })

        console.log("Printing message obj before setting the state: ")
        console.log(messageObj)
        // console.log(messageObj2)

        this.setState(prevState => ({ ...prevState, messages: [...prevState.messages, messageObj], app_list: values, app_values: values2 }))

    }

    updateChatbotState(message) {

        console.log("Updating chatbot state from action provider")

    // NOTE: This function is set in the constructor, and is passed in
    // from the top level Chatbot component. The setState function here
    // actually manipulates the top level state of the Chatbot, so it's
    // important that we make sure that we preserve the previous state.

        let fn = prevState => {
            return {
            ...prevState, messages: [...prevState.messages, message]
            }
        };
        this.setState(fn)

        //this.saveSingleMessage(message)
    }
}
export default ActionProvider;


