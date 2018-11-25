
/*** DataTable Script**/
var $TABLE = $('#table');
var $BTN = $('#export-btn');
var $EXPORT = $('#export');



$('.table-add').click(function () {
    // TODO : find better way to create row template
    var count = document.querySelectorAll(".properties_row").length;
    var template = " <tr class='properties_row'>\n" +
        "                                    <td class=\"pt-3-half\">\n" +
        "                                        <input type=\"hidden\" name=\"properties["+count+"].id\" value='-1'/>\n" +
        "                                        <input type=\"text\" name=\"properties["+count+"].name\" value='new_property'/>\n" +
        "                                    </td>\n" +
        "                                    <td>\n" +
        "                                        <span class=\"table-remove\">\n" +
        "                                            <button type=\"button\"\n" +
        "                                                    class=\"btn btn-danger btn-rounded btn-sm my-0\">Remove</button>\n" +
        "                                        </span>\n" +
        "                                    </td>\n" +
        "                                </tr>";
    $TABLE.find("table")
        .append(template);
});

$('.table-remove').click(function () {
    $(this).parents('tr').detach();
});

$('.table-up').click(function () {
    var $row = $(this).parents('tr');
    if ($row.index() === 1) return; // Don't go above the header
    $row.prev().before($row.get(0));
});

$('.table-down').click(function () {
    var $row = $(this).parents('tr');
    $row.next().after($row.get(0));
});

// A few jQuery helpers for exporting only
jQuery.fn.pop = [].pop;
jQuery.fn.shift = [].shift;

$BTN.click(function () {
    var $rows = $TABLE.find('tr:not(:hidden)');
    var headers = [];
    var data = [];

// Get the headers (add special header logic here)
    $($rows.shift()).find('th:not(:empty)').each(function () {
        headers.push($(this).text().toLowerCase());
    });

// Turn all existing rows into a loopable array
    $rows.each(function () {
        var $td = $(this).find('td');
        var h = {};

// Use the headers from earlier to name our hash keys
        headers.forEach(function (header, i) {
            h[header] = $td.eq(i).text();
        });

        data.push(h);
    });

// Output the result
    $EXPORT.text(JSON.stringify(data));
});

/*** End of DataTable Script**/

