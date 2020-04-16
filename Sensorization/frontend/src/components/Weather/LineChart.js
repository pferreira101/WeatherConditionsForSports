import React from "react";

// reactstrap components
import {
  Button,
  ButtonGroup,
  Card,
  CardHeader,
  CardBody,
  CardTitle,
  Row,
  Col,
} from "reactstrap";

// react plugin used to create charts
import { Line } from "react-chartjs-2";

// nodejs library that concatenates classes
import classNames from "classnames";

let toHours = (docs_ids) => {
  return docs_ids.map((id) => {
    let date = new Date(id)

    let hours = date.getHours();
    hours = date.getMinutes() > 30 ? hours+1 : hours;
    
    return `${hours}:00h`; //date.getMinutes() > 30 ? hours+1 : hours;
  })
}

let lineChartOptions = {
  maintainAspectRatio: false,
  legend: {
    display: false,
  },
  tooltips: {
    backgroundColor: "#f5f5f5",
    titleFontColor: "#333",
    bodyFontColor: "#666",
    bodySpacing: 4,
    xPadding: 12,
    mode: "nearest",
    intersect: 0,
    position: "nearest",
  },
  responsive: true,
  scales: {
    yAxes: [
      {
        barPercentage: 1.6,
        gridLines: {
          drawBorder: false,
          color: "rgba(29,140,248,0.0)",
          zeroLineColor: "transparent",
        },
        ticks: {
          suggestedMin: 0,
          suggestedMax: 35,
          padding: 20,
          fontColor: "#9a9a9a",
        },
      },
    ],
    xAxes: [
      {
        barPercentage: 1.6,
        gridLines: {
          drawBorder: false,
          color: "rgba(29,140,248,0.1)",
          zeroLineColor: "transparent",
        },
        ticks: {
          padding: 20,
          fontColor: "#9a9a9a",
        },
      },
    ],
  },
};

let weatherChart = (canvas) => {
  let ctx = canvas.getContext("2d");
  let gradientStroke = ctx.createLinearGradient(0, 230, 0, 50);

  gradientStroke.addColorStop(1, "rgba(29,140,248,0.2)");
  gradientStroke.addColorStop(0.4, "rgba(29,140,248,0.0)");
  gradientStroke.addColorStop(0, "rgba(29,140,248,0)"); //blue colors

  return {
    labels: labels,
    datasets: [
      {
        label: "(ºC)",
        fill: true,
        backgroundColor: gradientStroke,
        borderColor: "#1f8ef1",
        borderWidth: 2,
        borderDash: [],
        borderDashOffset: 0.0,
        pointBackgroundColor: "#1f8ef1",
        pointBorderColor: "rgba(255,255,255,0)",
        pointHoverBackgroundColor: "#1f8ef1",
        pointBorderWidth: 20,
        pointHoverRadius: 4,
        pointHoverBorderWidth: 15,
        pointRadius: 4,
        data: displayData === "weather" ? [...weather] : [...feelsLike],
      },
    ],
  };
};

let labels = [];
let weather = [];
let feelsLike = [];
let displayData = "";

class LineChart extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      title: this.props.title,
      subtitle: this.props.subtitle,
      labels: toHours(this.props.labels),
      weather: this.props.weatherData,
      feelsLike: this.props.feelsLikeData,
      displayData: "weather",
    };
    labels = this.state.labels;
    weather = this.state.weather;
    feelsLike = this.state.feelsLike;
    displayData = "weather";
  }

  setDisplayData = (name) => {
    this.setState({
      displayData: name,
    });
    displayData = name;
  };

  render() {
    return (
      <Row>
        <Col xs="12">
          <Card className="card-chart">
            <CardHeader>
              <Row>
                <Col className="text-left" sm="6">
                  <h5 className="card-category">
                    {this.state.subtitle}
                  </h5>
                  <CardTitle tag="h2">{this.state.title}</CardTitle>
                </Col>
                <Col sm="6">
                  <ButtonGroup
                    className="btn-group-toggle float-right"
                    data-toggle="buttons"
                  >
                    <Button
                      tag="label"
                      className={classNames("btn-simple", {
                        active: this.state.displayData === "weather",
                      })}
                      color="info"
                      id="0"
                      size="sm"
                      onClick={() => this.setDisplayData("weather")}
                    >
                      <input
                        defaultChecked
                        className="d-none"
                        name="options"
                        type="radio"
                      />
                      <span className="d-none d-sm-block d-md-block d-lg-block d-xl-block">
                        Weather
                      </span>
                      <span className="d-block d-sm-none">
                        <i className="tim-icons icon-single-02" />
                      </span>
                    </Button>
                    <Button
                      color="info"
                      id="1"
                      size="sm"
                      tag="label"
                      className={classNames("btn-simple", {
                        active: this.state.displayData === "feelsLike",
                      })}
                      onClick={() => this.setDisplayData("feelsLike")}
                    >
                      <input className="d-none" name="options" type="radio" />
                      <span className="d-none d-sm-block d-md-block d-lg-block d-xl-block">
                        Feels Like
                      </span>
                      <span className="d-block d-sm-none">
                        <i className="tim-icons icon-gift-2" />
                      </span>
                    </Button>
                  </ButtonGroup>
                </Col>
              </Row>
            </CardHeader>
            <CardBody>
              <div className="chart-area">
                <Line data={weatherChart} options={lineChartOptions} />
              </div>
            </CardBody>
          </Card>
        </Col>
      </Row>
    );
  }
}

export default LineChart;
