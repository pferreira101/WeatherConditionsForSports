import React from "react";

// reactstrap components
import { Spinner } from "reactstrap";

// core components
import LineChart from "components/LineChart.js";

// firestore
import firebase from "firebase/firestore.js";

class Weather extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      ids: [],
      weatherData: [],
      feelsLikeData: [],
    };

    this.getWeatherData();
  }

  getWeatherData() {
    const db = firebase.firestore();

    db.collection("WM")
      .limit(24)
      .get()
      .then((querySnapshot) => {
        let docs = querySnapshot.docs;
        let docs_data = docs.map((doc) => doc.data());
        console.log(docs_data);
        this.setState({
          ids: docs.map((doc) => doc.id),
          weatherData: docs_data.map((data) => data["temp_max"]),
          feelsLikeData: docs_data.map((data) => data["feels_like"]),
        });
      });
  }

  render() {
    if (
      this.state.ids.length === 0 ||
      this.state.feelsLikeData.length === 0 ||
      this.state.weatherData.length === 0
    ) {
      //isto ainda nao funciona
      return (
        <div className="content">
          <Spinner style={{ width: "3rem", height: "3rem" }} color="danger" />
        </div>
      );
    }
    return (
      <div className="content">
        <LineChart
          title="Weather"
          subtitle="Braga - Semana 13/04"
          labels={this.state.ids}
          weatherData={this.state.weatherData}
          feelsLikeData={this.state.feelsLikeData}
        />
      </div>
    );
  }
}

export default Weather;
