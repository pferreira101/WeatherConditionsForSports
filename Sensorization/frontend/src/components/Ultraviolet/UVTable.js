import React from "react";

// reactstrap components
import {
  Card,
  CardHeader,
  CardBody,
  CardTitle,
  Col,
  Table,
  Row,
} from "reactstrap";

let formatLabels = (docs_ids) => {
  return docs_ids.map((id) => {
    let date = new Date(id);

    let x = new String(date.getHours());
    if (x == "NaN") return new String("-");

    let hours = date.getHours();
    hours = date.getMinutes() > 30 ? hours + 1 : hours;

    let hourString = hours > 9 ? hours : `0${hours}`; // garantir que tem sempre 2 digitios para as colunas da tabela fiquem iguais
    let day = id.slice(8, 10);
    let month = id.slice(5, 7);

    return `${day}/${month} ${hourString}:00h `;
  });
};

let onlySunValues = (labels, st1, st2, st3, st4, st5, st6) => {
  let ids = [];
  let sunvalues1 = [];
  let sunvalues2 = [];
  let sunvalues3 = [];
  let sunvalues4 = [];
  let sunvalues5 = [];
  let sunvalues6 = [];

  let i;
  for (i = 0; i < st1.length; i++) {
    if (st1[i] === "None" && st1[i + 1] !== "None") {
      ids.push("-");
      sunvalues1.push("-");
      sunvalues2.push("-");
      sunvalues3.push("-");
      sunvalues4.push("-");
      sunvalues5.push("-");
      sunvalues6.push("-");
    }
    if (st1[i] !== "None") {
      ids.push(labels[i]);
      sunvalues1.push(st1[i]);
      sunvalues2.push(st2[i]);
      sunvalues3.push(st3[i]);
      sunvalues4.push(st4[i]);
      sunvalues5.push(st5[i]);
      sunvalues6.push(st6[i]);
    }
  }

  return {
    labels: ids,
    st1: sunvalues1,
    st2: sunvalues2,
    st3: sunvalues3,
    st4: sunvalues4,
    st5: sunvalues5,
    st6: sunvalues6,
  };
};

let getData = (specificDay, labels, st1, st2, st3, st4, st5, st6) => {
  if (!specificDay)
    return {
      labels: formatLabels(labels.slice(-24)),
      st1: st1.slice(-24),
      st2: st2.slice(-24),
      st3: st3.slice(-24),
      st4: st4.slice(-24),
      st5: st5.slice(-24),
      st6: st6.slice(-24)
    }

  let _labels = [], _st1 = [], _st2 = [], _st3 = [], _st4 = [], _st5 = [], _st6 = [];

  for (let i in labels) {
    if ((new Date(labels[i])).toDateString() == specificDay) {
      _labels.push(labels[i])
      _st1.push(st1[i])
      _st2.push(st2[i])
      _st3.push(st3[i])
      _st4.push(st4[i])
      _st5.push(st5[i])
      _st6.push(st6[i])
    }
  }

  return {
    labels: formatLabels(_labels), 
    st1: _st1, 
    st2: _st2, 
    st3: _st3, 
    st4: _st4, 
    st5: _st5, 
    st6: _st6}
}

class UVTable extends React.Component {
  constructor(props) {
    super(props);
    
    let data = getData(this.props.specificDay, this.props.labels, this.props.st1, this.props.st2, this.props.st3, this.props.st4, this.props.st5, this.props.st6);

    data = onlySunValues(data.labels, data.st1, data.st2, data.st3, data.st4, data.st5, data.st6);

    this.state = {
      labels: data.labels,
      st1: data.st1,
      st2: data.st2,
      st3: data.st3,
      st4: data.st4,
      st5: data.st5,
      st6: data.st6,
    };
  }
  render() {
    return (
      <Row>
        <Col md="12">
          <Card>
            <CardHeader>
              <CardTitle tag="h4">Safe exposure time per skin type (min)</CardTitle>
            </CardHeader>
            <CardBody>
              <Table className="tablesorter" responsive>
                <thead className="text-primary">
                  <tr>
                    <th>Type</th>
                    {this.state.labels.map((label) => (
                      <th className="text-secondary text-center"> {label}</th>
                    ))}
                  </tr>
                </thead>
                <tbody>
                  <tr>
                    <th className="text-secondary text-center" scope="row">
                      I
                    </th>
                    {this.state.st1.map((value) => (
                      <td className="text-center">{`${value}`}</td>
                    ))}
                  </tr>
                  <tr>
                    <th className="text-secondary text-center" scope="row">
                      II
                    </th>
                    {this.state.st2.map((value) => (
                      <td className="text-center">{`${value}`}</td>
                    ))}
                  </tr>
                  <tr>
                    <th className="text-secondary text-center" scope="row">
                      III
                    </th>
                    {this.state.st3.map((value) => (
                      <td className="text-center">{`${value}`}</td>
                    ))}
                  </tr>
                  <tr>
                    <th className="text-secondary text-center" scope="row">
                      IV
                    </th>
                    {this.state.st4.map((value) => (
                      <td className="text-center">{`${value}`}</td>
                    ))}
                  </tr>
                  <tr>
                    <th className="text-secondary text-center" scope="row">
                      V
                    </th>
                    {this.state.st5.map((value) => (
                      <td className="text-center">{`${value}`}</td>
                    ))}
                  </tr>
                  <tr>
                    <th className="text-secondary text-center" scope="row">
                      VI
                    </th>
                    {this.state.st6.map((value) => (
                      <td className="text-center">{`${value}`}</td>
                    ))}
                  </tr>
                </tbody>
              </Table>
            </CardBody>
          </Card>
        </Col>
      </Row>
    );
  }
}

export default UVTable;
