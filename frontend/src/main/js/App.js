import React from 'react';
import FestivalClient from "./clients/festival_client";

class App extends React.Component {

    constructor() {
        super();
        this.state = {
            artistsToAdd: "",
            event: null
        };
        this.submitFestival = this.submitFestival.bind(this);
        this.artistInputChanged = this.artistInputChanged.bind(this);
    }

    render() {
        return (
            <div>
                <h1>Festival Compare</h1>
                <textarea
                    data-qa="artist-input"
                    value={this.state.artistsToAdd}
                    onChange={this.artistInputChanged}/>
                <button data-qa="submit"
                        onClick={this.submitFestival}>Submit</button>

                { this.state.event !== null &&
                    <div>
                        <h2>{this.state.event.eventName} - <span data-qa="festival-score">{this.state.event.eventScore}</span></h2>

                        {/*TODO - order by popuarity, (hilights section?)*/}
                        {this.state.event.artists.map((artist) => {
                            // TODO test case where no scores found
                            return <div>
                                <span key={artist.name}>{artist.name}</span>
                                {artist.scores.map((score) => {
                                    return <span key={artist.name + score.type}> {score.type}: {score.score}</span>
                                })}
                                </div>
                        })}
                    </div>
                }
            </div>
        );
    }

    artistInputChanged(event) {
        this.setState({artistsToAdd: event.target.value});
    }

    submitFestival() {
        let client = new FestivalClient();
        client.createFestival({
            eventName: "TODO fest",
            // TODO test this and also trim
            artistNames: this.state.artistsToAdd.split('\n')
        }).then((event) => {
            this.setState({event: event});
        })
    }


}

export default App;