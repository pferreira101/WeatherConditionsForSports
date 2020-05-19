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

let formatLabels = (hours) => {
  return hours.map((hour) => {
    return `${hour}:00h`;
  });
};

let formatData = (data) => {
  return data.map((x) => {
    return x.toFixed(1);
  });
};

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
          suggestedMin: 5,
          suggestedMax: 5,
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

let forecastChart = (canvas) => {
  let ctx = canvas.getContext("2d");

  let gradientStroke = ctx.createLinearGradient(0, 230, 0, 50);

  gradientStroke.addColorStop(1, "rgba(29,140,248,0.2)");
  gradientStroke.addColorStop(0.4, "rgba(29,140,248,0.0)");
  gradientStroke.addColorStop(0, "rgba(29,140,248,0)"); //blue colors

  return {
    labels: hours,
    datasets: [
      {
        label:
          displayData === "weather"
            ? "(ºC)"
            : displayData === "humidity"
            ? "%"
            : "UV",
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
        data:
          displayData === "weather"
            ? [...weather]
            : displayData === "humidity"
            ? [...humidity]
            : [...uv],
      },
    ],
  };
};

let hours = [];
let humidity = [];
let uv = [];
let weather = [];
let displayData = "";

class LineChart extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      title: this.props.title,
      subtitle: this.props.subtitle,
      hours: formatLabels(this.props.hours),
      humidity: formatData(this.props.humidity),
      uv: formatData(this.props.uv),
      weather: formatData(this.props.weather),
      displayData: "weather",
    };
    hours = this.state.hours;
    humidity = this.state.humidity;
    uv = this.state.uv;
    weather = this.state.weather;
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
                  <h5 className="card-category">{this.state.subtitle}</h5>
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
                        Feels Like
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
                        active: this.state.displayData === "humidity",
                      })}
                      onClick={() => this.setDisplayData("humidity")}
                    >
                      <input className="d-none" name="options" type="radio" />
                      <span className="d-none d-sm-block d-md-block d-lg-block d-xl-block">
                        Humidity
                      </span>
                      <span className="d-block d-sm-none">
                        <i className="tim-icons icon-gift-2" />
                      </span>
                    </Button>
                    <Button
                      tag="label"
                      className={classNames("btn-simple", {
                        active: this.state.displayData === "uv",
                      })}
                      color="info"
                      id="0"
                      size="sm"
                      onClick={() => this.setDisplayData("uv")}
                    >
                      <input
                        defaultChecked
                        className="d-none"
                        name="options"
                        type="radio"
                      />
                      <span className="d-none d-sm-block d-md-block d-lg-block d-xl-block">
                        UV
                      </span>
                      <span className="d-block d-sm-none">
                        <i className="tim-icons icon-single-02" />
                      </span>
                    </Button>
                  </ButtonGroup>
                </Col>
              </Row>
            </CardHeader>
            <CardBody>
              <div className="chart-area">
                <Line data={forecastChart} options={lineChartOptions} />
              </div>
            </CardBody>
          </Card>
        </Col>
      </Row>
    );
  }
}

export default LineChart;
