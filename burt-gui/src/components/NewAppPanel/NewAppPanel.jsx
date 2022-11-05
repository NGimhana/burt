import React, {useEffect, useRef, useState} from "react";
import 'bootstrap/dist/css/bootstrap.min.css';
import './NewAppPanel.css'
import Modal from "react-modal";
import axios from "axios";


const customStyles = {
    content: {
        top: '50%',
        left: '50%',
        right: 'auto',
        bottom: 'auto',
        marginRight: '-50%',
        transform: 'translate(-50%, -50%)',
        borderRadius: '5px',
    },
    modalButton: {
        margin: '.2em',
    }

}

class NewAppPanel extends React.Component {

    constructor(props) {
        super(props)
        // preserve the initial state in a new object
        this.baseState = this.state
    }

    state = {
        password: "",
        appName: "",
        appVersion: "",
        isOpen: false,
        isOpenAppPanel: this.props.isOpenAppPanel,
        selectedIcon: 'none',
        selectedCrashscopeFile: 'none',
        selectedTracereplayerFile: 'none',
        fileUploaded: false,
        fileUploadSubmitted: false,
        isValid: false,
        isError: false
    }

    showHidePasswordField() {
        let div = document.getElementById("name");
        div.style.display = (div.style.display !== "none") ? "none" : "block";
    }

    handleChange = event => {
        this.setState({
            password: event.target.value
        })
    };

    handleAppNameChange = event => {
        this.setState({
            appName: event.target.value
        })
    };

    handleAppVersionChange = event => {
        this.setState({
            appVersion: event.target.value
        })
    };


    toggleModal = () => {
        this.setState({
            isOpen: !this.state.isOpen
        })
    }

    validatePassword = event => {

        this.setState({
            password: event.target.value
        })

        // TODO : default password change this functionality to backend
        var defaultPassword = "test"
        if (this.state.password === defaultPassword) {
            this.toggleModal()
            this.toggleAddNewAppModal()
        } else {
            alert("Password Not Matched")
        }
    };

    toggleAddNewAppModal = () => {
        this.setState({
            isOpenAppPanel: !this.state.isOpenAppPanel
        })
    }

    validateSubmitButton = () => {
        return this.state.selectedIcon !== 'none' && this.state.selectedCrashscopeFile !== 'none'
    }


    submitNewApp = () => {

        // Get new App Add endpoint
        const endPoint = this.props.config.serverEndpoint + this.props.config.addAppService;


        // if (!this.state.isError) {

        this.setState({
            fileUploadSubmitted: true
        })

        // Prepare data
        const sessionId = this.props.sessionId;
        const data = {
            sessionId: sessionId,
            messages: [{
                message: this.state.fullStepDescription,
                selectedValues: [this.props.index]
            }]
        }


        const formData = new FormData();
        formData.append("req", new Blob([JSON.stringify(data)], {
            type: "application/json"
        }));
        formData.append("image", this.state.selectedIcon);
        formData.append("crashScopeZip", this.state.selectedCrashscopeFile);
        formData.append("traceReplayerZip", this.state.selectedTracereplayerFile);

        // Send and process post request
        const responsePromise = axios.post(endPoint, formData);
        responsePromise.then(response => {

            let result = response.data;
            if (!result) {
                console.error(`The New App Data was not updated: ` + this.props.index);
            } else {
                this.setState({
                    fileUploaded: true
                })
                this.close()
                alert("Files Uploaded Successfully! Please Restart the Conversation to see the new App")

            }
        }).catch(error => {
            console.error(`There was an unexpected error: ${error}`);

        })

    }


    changeIconHandler = event => {
        this.setState({
            selectedIcon: event.target.files[0]
        })

        console.log(this.state.selectedIcon)
    }

    changeCrashscopeHandler = event => {
        this.setState({
            selectedCrashscopeFile: event.target.files[0]
        }, () => console.log("Crash Scope File", this.state.selectedCrashscopeFile.name))

    }

    changeTracereplayerHandler = event => {
        this.setState({
            selectedTracereplayerFile: event.target.files[0]
        })
    }

    onInputchange(event) {
        this.setState({
            [event.target.name]: event.target.value
        });
    }

    close = () => {
        this.setState(this.baseState)
    }

    open() {
        console.log(this.state.isOpen)
        this.setState({isOpen: true});
    }

    validateAppNameWithFile = (file) => {
        let appName = this.state.appName.concat("-").concat(this.state.appVersion)
        if (file === this.state.selectedCrashscopeFile) {
            this.setState({
                isError: true
            })
        } else {
            this.setState({
                isError: true
            })
        }
    }

    render() {
        return (
            <div>
                <Modal
                    isOpen={this.state.isOpenAppPanel}
                    contentLabel="My dialog"
                    onRequestClose={this.toggleAddNewAppModal}
                    style={customStyles}
                    backdrop="static"
                    keyboard={false}
                    closeTimeoutMS={10}>
                    <div className={"popup-display"}>
                        <div className="subpanel-header">
                            Developer Panel
                        </div>
                        <div id={"name"} className="addNewApp" style={{padding: '5px'}}>
                            <input type={"password"} placeholder={"Enter password"} value={this.state.password}
                                   onChange={this.handleChange}/>
                            <button className={"btn-primary"} onClick={this.validatePassword}>Submit</button>
                        </div>
                    </div>
                </Modal>


                <Modal
                    isOpen={this.state.isOpen}
                    onRequestClose={this.toggleModal}
                    contentLabel="My dialog"
                    style={customStyles}
                    backdrop="static"
                    keyboard={false}
                    closeTimeoutMS={10}>
                    <div className={"popup-display"}>
                        <div>
                            <div className="form-label"><h3>App Details</h3></div>
                            <div className="container">
                                <p>
                                    <span style={{color: 'red', fontSize: '20px'}}>* </span>
                                    indicates a required field
                                </p>

                                <div className={"form-group row"}>
                                    <label htmlFor="appIcon" className={"col-sm-4 col-form-label form-label"}>App icon
                                        <span style={{color: 'red', fontSize: '20px'}}>*</span></label>
                                    <div className={"col-sm-8"}>
                                        <input type="file" className="form-control-file"
                                               onChange={this.changeIconHandler} accept="image/*" id="appIcon"/>
                                    </div>
                                </div>

                                <div className={"form-group row"}>
                                    <label htmlFor="crashScopeFile" className={"col-sm-4 col-form-label form-label"}>CrashScope
                                        Zip File
                                        <span style={{color: 'red', fontSize: '20px'}}>*</span></label>

                                    <div className={"col-sm-8"}>
                                        <input type="file" className="form-control-file"
                                               onChange={this.changeCrashscopeHandler} accept=".zip"
                                               id="crashScopeFile"/>
                                    </div>
                                </div>
                                <div className={"form-group row"}>
                                    <label htmlFor="traceReplayerFile" className={"col-sm-4 col-form-label form-label"}>
                                        TraceReplayer ZipFile
                                    </label>
                                    <div className={"col-sm-8"}>
                                        <input type="file" className="form-control-file"
                                               onChange={this.changeTracereplayerHandler} accept=".zip"
                                               id="traceReplayerFile"/>
                                    </div>
                                </div>

                                <div className={"form-group row"}>
                                    <div className={"col-sm-2"}>
                                        <button className={"btn btn-danger btn-sm"} onClick={this.close}>Cancel
                                        </button>
                                    </div>
                                    <div className={"col-sm-4 submitButton"}>
                                        <button className={"btn btn-primary btn-sm"}
                                                disabled={!this.validateSubmitButton()}
                                                onClick={this.submitNewApp}>Submit
                                            {
                                                this.state.fileUploadSubmitted && !this.state.fileUploaded &&
                                                <span className="spinner-border spinner-border-sm"
                                                      style={{marginLeft: '10px'}}
                                                      role="status"
                                                      aria-hidden="true"></span>
                                            }
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </Modal>
            </div>
        )
    }
}

export default NewAppPanel;