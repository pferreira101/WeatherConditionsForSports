import React from "react";

// reactstrap components
import { Card, CardHeader, CardBody, CardTitle, Col, Table } from "reactstrap";

let formatLabels = (docs_ids) => {
  return docs_ids.map((id) => {
    let date = new Date(id);

    let hours = date.getHours();
    hours = date.getMinutes() > 30 ? hours + 1 : hours;

    let hourString = hours > 9 ? hours : `0${hours}`; // garantir que tem sempre 2 digitios para as colunas da tabela fiquem iguais
    let day = id.slice(8, 10);
    let month = id.slice(5, 7);

    return `${day}/${month} ${hourString}:00h `;
  });
};


let getData = (specificDay, labels, icons, humidityData, windSpeedData, windDegData) => {

  if (!specificDay)
    return [formatLabels(labels.slice(-24)), icons.slice(-24), humidityData.slice(-24), windSpeedData.slice(-24), windDegData.slice(-24)]

  let _labels = [], _icons = [], _humidityData = [], _windSpeedData = [], _windDegData = []

  for (let i in labels) {
    if ((new Date(labels[i])).toDateString() == specificDay) {
      _labels.push(labels[i])
      _icons.push(icons[i])
      _humidityData.push(humidityData[i])
      _windSpeedData.push(windSpeedData[i])
      _windDegData.push(windDegData[i])
    }
  }

  return [formatLabels(_labels), _icons, _humidityData, _windSpeedData, _windDegData]
}


class WeatherTable extends React.Component {
  constructor(props) {
    super(props);

    let data = getData(this.props.specificDay, this.props.labels, this.props.icons, this.props.humidityData, this.props.windSpeedData, this.props.windDegData)
    let labels = data[0], icons = data[1], humidityData = data[2], windSpeedData = data[3], windDegData = data[4]

    this.state = {
      labels: labels,
      icons: icons,
      humidityData: humidityData,
      windSpeedData: windSpeedData,
      windDegData: windDegData,
      specificDay: this.props.specificDay,
    };
  }
  render() {
    return (
      <Col md="12">
        <Card>
          <CardHeader>
            <CardTitle tag="h3">Conditions</CardTitle>
          </CardHeader>
          <CardBody>
            <Table className="tablesorter" responsive>
              <thead className="text-primary">
                <tr>
                  <th></th>
                  {this.state.labels.map((label) => (
                    <th className="text-secondary text-center"> {label}</th>
                  ))}
                </tr>
              </thead>
              <tbody>
                <tr>
                  <th className="text-secondary" scope="row">
                    Condition
                  </th>
                  {this.state.icons.map((icon) => {
                    
                    return icon !== '-' ?
                      <td>
                        <img
                          alt=""
                          src={`http://openweathermap.org/img/wn/${icon}@2x.png`}
                        />
                      </td>
                    : 
                    <td text-center>-</td>})}
                </tr>
                <tr>
                  <th className="text-secondary text-center" scope="row">
                    Humidity (%)
                  </th>
                  {this.state.humidityData.map((humidity) => (
                    <td className="text-center">{`${humidity}`}</td>
                  ))}
                </tr>
                <tr>
                  <th className="text-secondary text-center" scope="row">
                    Wind Speed (km/h)
                  </th>
                  {this.state.windSpeedData.map((windSpeed) => (
                    <td className="text-center">
                      {(windSpeed * 3.6).toFixed(1)}
                    </td>
                  ))}
                </tr>
                <tr>
                  <th className="text-secondary text-center" scope="row">
                    Wind Direction
                  </th>
                  {this.state.windDegData.map((windDeg) =>
                    windDeg === "-" ? (
                      <td className="text-center">-</td>
                    ) : (
                        <td>
                          <img
                            style={{ transform: `rotate(${windDeg + 90}deg)` }}
                            alt=""
                            src={`https://svn.apache.org/repos/asf/openoffice/symphony/trunk/main/extras/source/gallery/arrows/A07-Arrow-LightBlue-Left.png`}
                          />
                        </td>
                      )
                  )}
                </tr>
              </tbody>
            </Table>
          </CardBody>
        </Card>
      </Col>
    );
  }
}

export default WeatherTable;
