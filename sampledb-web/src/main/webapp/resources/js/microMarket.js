const API_URL = "/sampledb/api/micro-markets";
const AUTH_HEADER = "Basic " + btoa("manager:123");

let isEditMode = false;

document.addEventListener("DOMContentLoaded", () => {
    loadTableData();
    document.getElementById("market-form").addEventListener("submit", handleSave);
    document.getElementById("btn-clear").addEventListener("click", clearForm);
    document.getElementById("btn-delete").addEventListener("click", handleDelete);
});

// FETCH GET: Carrega os dados da tabela
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
                const tbody = document.getElementById("market-table-body");
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
                                <a href="#" onclick="selectRecord('${item.zipCode}', ${item.radius}); return false;" 
                                   class="text-blue-600 hover:text-blue-800 font-bold hover:underline">
                                    ${item.zipCode}
                                </a>
                            </td>
                            <td class="p-3 text-gray-600">${parseFloat(item.radius).toFixed(1)}</td>
                        `;
                    tbody.appendChild(row);
                });
            })
            .catch(err => showMessage("error-box", err.message));
}

// Ação: Preenche o formulário ao clicar na linha da tabela
function selectRecord(zip, radius) {
    document.getElementById("zipCode").value = zip;
    document.getElementById("zipCode").disabled = true; // Chave primária travada na edição
    document.getElementById("radius").value = radius;

    document.getElementById("form-title").innerText = "Update Micro Market: " + zip;
    document.getElementById("btn-delete").classList.remove("hidden");
    isEditMode = true;
    hideMessages();
}

// FETCH POST: Envia o payload do Record DTO para salvar/atualizar
function handleSave(event) {
    event.preventDefault();
    hideMessages();

    const payload = {
        zipCode: document.getElementById("zipCode").value.trim(),
        radius: parseFloat(document.getElementById("radius").value)
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
                    throw new Error("Transaction processing rejected by Core business layer.");
                showMessage("success-box", isEditMode ? "Market updated successfully!" : "New market created successfully!");
                clearForm();
                loadTableData();
            })
            .catch(err => showMessage("error-box", err.message));
}

// FETCH DELETE: Remove o mercado com base na chave primária
function handleDelete() {
    const zip = document.getElementById("zipCode").value;
    if (!zip || !confirm(`Are you sure you want to permanently drop micro market "${zip}"?`))
        return;

    hideMessages();

    fetch(`${API_URL}/${zip}`, {
        method: "DELETE",
        headers: {"Authorization": AUTH_HEADER}
    })
            .then(res => {
                if (!res.ok)
                    throw new Error("Deletion rejected. Code may be bound to an active Customer.");
                showMessage("success-box", "Micro Market dropped completely.");
                clearForm();
                loadTableData();
            })
            .catch(err => showMessage("error-box", err.message));
}

// Utilitários de Reset da Interface
function clearForm() {
    document.getElementById("market-form").reset();
    document.getElementById("zipCode").disabled = false;
    document.getElementById("form-title").innerText = "New Micro Market";
    document.getElementById("btn-delete").classList.add("hidden");
    isEditMode = false;
}

// Exibe alertas temporários de feedback
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