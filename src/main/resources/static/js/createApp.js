var windowH = window.innerHeight;
document.body.innerHTML += "<style>#cy{height: " + windowH * .8 + "px}</style>"
var cy = cytoscape({
    container: document.getElementById('cy'),
    elements: []
});
cy.on('click', 'node', function (evt) {
    $('#propBody').remove();
    var operationId = this.data().operatorId;
    $.getJSON("/rest/getavailableproperties?opId=" + (operationId || ""), function (data) {
        console.log(data);
        var ht = "<tbody id='propBody'></tbody>";
        $('#propTable').append(ht);
        for (var item in data) {
            var name = data[item].name;
            var html = "<tr><td>" + name + "</td><td><input type='text'></td></tr>";
            $('#propTable').append(html);
        }

    })


});

var updateOpertaionsPanel = function (sourceOpId) {
    $.getJSON("/rest/getavailableoperations?sId=" + (sourceOpId || ""), function (data) {
        console.log(data);
        for (var item in data) {
            var name = data[item].name;
            var btn = document.createElement("BUTTON");
            btn.innerText = name;
            btn.classList += "btn btn-info";
            document.getElementById("tools_inner").appendChild(btn);
            btn.addEventListener('click', function () {
                cy.add({
                    group: "nodes",
                    data: {
                        label: name,
                        operatorId: data[item].id
                    },
                    position: {x: 200, y: 200}
                });
            });
            cy.style().selector('node')
                .style({
                    'label': 'data(label)'
                }).update()
        }
    });
}
updateOpertaionsPanel();

