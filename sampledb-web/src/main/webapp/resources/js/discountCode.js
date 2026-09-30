const API_URL = "/sampledb/api/discount-codes";
const AUTH_HEADER = "Basic " + btoa("manager:123");

let isEditMode = false;

document.addEventListener("DOMContentLoaded", () => {
    loadTableData();
    document.getElementById("discount-form").addEventListener("submit", handleSave);
    document.getElementById("btn-clear").addEventListener("click", clearForm);
    document.getElementById("btn-delete").addEventListener("click", handleDelete);
});

function loadTableData() {
    fetch(API_URL, {
        method: "GET",
        headers: {"Authorization": AUTH_HEADER, "Accept": "application/json"}
    })
            .then(res => {
                if (res.status === 401 || res.status === 403)
                    throw new Error("Access Denied: Check roles privileges.");
                if (!res.ok)
                    throw new Error("Server error while reading database records.");
                return res.json();
            })
            .then(data => {
                const tbody = document.getElementById("discount-table-body");
                tbody.innerHTML = "";

                if (data.length === 0) {
                    tbody.innerHTML = `<tr><td colspan="2" class="p-4 text-center text-gray-400 italic">No historical records found.</td></tr>`;
                    return;
                }

                data.forEach(item => {
                    const row = document.createElement("tr");
                    row.className = "hover:bg-gray-50 transition";
                    row.innerHTML = `
                            <td class="p-3">
                                <a href="#" onclick="selectRecord('${item.discountCode}', ${item.rate}); return false;" 
                                   class="text-blue-600 hover:text-blue-800 font-bold hover:underline">
                                    ${item.discountCode}
                                </a>
                            </td>
                            <td class="p-3 text-gray-600">${parseFloat(item.rate).toFixed(2)}%</td>
                        `;
                    tbody.appendChild(row);
                });
            })
            .catch(err => showMessage("error-box", err.message));
}

function selectRecord(code, rate) {
    document.getElementById("discountCode").value = code;
    document.getElementById("discountCode").disabled = true;
    document.getElementById("rate").value = rate;

    document.getElementById("form-title").innerText = "Update Discount Code: " + code;
    document.getElementById("btn-delete").classList.remove("hidden");
    isEditMode = true;
    hideMessages();
}

function handleSave(event) {
    event.preventDefault();
    hideMessages();

    const payload = {
        discountCode: document.getElementById("discountCode").value.toUpperCase().trim(),
        rate: parseFloat(document.getElementById("rate").value)
    };

    fetch(API_URL, {
        method: "POST",
        headers: {
            "Authorization": AUTH_HEADER,
            "Content-Type": "application/json"
        },
        body: JSON.stringify(payload)
    })
            .then(res => {
                if (!res.ok)
                    throw new Error("Failed to process transaction logic inside Core module.");
                showMessage("success-box", isEditMode ? "Record updated successfully!" : "New record created successfully!");
                clearForm();
                loadTableData();
            })
            .catch(err => showMessage("error-box", err.message));
}

function handleDelete() {
    const code = document.getElementById("discountCode").value;
    if (!code || !confirm(`Are you sure you want to permanently drop code "${code}"?`))
        return;

    hideMessages();

    fetch(`${API_URL}/${code}`, {
        method: "DELETE",
        headers: {"Authorization": AUTH_HEADER}
    })
            .then(res => {
                if (!res.ok)
                    throw new Error("Could not remove code. Foreign key constraint violation might occurred.");
                showMessage("success-box", "Record removed completely from system database.");
                clearForm();
                loadTableData();
            })
            .catch(err => showMessage("error-box", err.message));
}

function clearForm() {
    document.getElementById("discount-form").reset();
    document.getElementById("discountCode").disabled = false;
    document.getElementById("form-title").innerText = "New Discount Code";
    document.getElementById("btn-delete").classList.add("hidden");
    isEditMode = false;
}

function showMessage(elementId, text) {
    const box = document.getElementById(elementId);
    box.innerText = text;
    box.classList.remove("hidden");
    setTimeout(() => box.classList.add("hidden"), 5000);
}

function hideMessages() {
    document.getElementById("error-box").classList.add("hidden");
    document.getElementById("success-box").classList.add("hidden");
}