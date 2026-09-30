const API_URL = "/sampledb/api/product-codes";
const DISCOUNT_API_URL = "/sampledb/api/discount-codes";
const AUTH_HEADER = "Basic " + btoa("manager:123");

let isEditMode = false;

document.addEventListener("DOMContentLoaded", () => {
    // Inicialização coordenada da tela
    loadDiscountCombo();
    loadTableData();

    document.getElementById("product-code-form").addEventListener("submit", handleSave);
    document.getElementById("btn-clear").addEventListener("click", clearForm);
    document.getElementById("btn-delete").addEventListener("click", handleDelete);
});

// Chamada Assíncrona: Popula a combo-box com dados vindos de outra API
function loadDiscountCombo() {
    fetch(DISCOUNT_API_URL, {
        method: "GET",
        headers: {"Authorization": AUTH_HEADER, "Accept": "application/json"}
    })
            .then(res => {
                if (!res.ok)
                    throw new Error();
                return res.json();
            })
            .then(data => {
                const select = document.getElementById("discountCode");
                select.innerHTML = '<option value="">Select a Discount Tier</option>';

                data.forEach(item => {
                    const opt = document.createElement("option");
                    opt.value = item.discountCode;
                    opt.innerText = `${item.discountCode} (${parseFloat(item.rate).toFixed(2)}%)`;
                    select.appendChild(opt);
                });
            })
            .catch(() => {
                document.getElementById("discountCode").innerHTML = '<option value="">Failed to sync tiers</option>';
            });
}

// Chamada Assíncrona: Lista os códigos de produto e resolve o relacionamento lógico
function loadTableData() {
    fetch(API_URL, {
        method: "GET",
        headers: {"Authorization": AUTH_HEADER, "Accept": "application/json"}
    })
            .then(res => {
                if (!res.ok)
                    throw new Error("Could not fetch product codes.");
                return res.json();
            })
            .then(data => {
                const tbody = document.getElementById("product-code-table-body");
                tbody.innerHTML = "";

                data.forEach(item => {
                    const row = document.createElement("tr");
                    row.className = "hover:bg-gray-50 transition";

                    // Tratamento seguro para o relacionamento @ManyToOne lógico sem FK física
                    const associatedDiscount = item.discountCode ? item.discountCode.discountCode : "None";

                    row.innerHTML = `
                            <td class="p-3">
                                <a href="#" onclick="selectRecord('${item.prodCode}', '${item.description}', '${associatedDiscount}'); return false;" 
                                   class="text-blue-600 hover:text-blue-800 font-bold hover:underline">
                                    ${item.prodCode}
                                </a>
                            </td>
                            <td class="p-3 text-gray-600">${item.description}</td>
                            <td class="p-3 text-gray-500 font-medium">${associatedDiscount}</td>
                        `;
                    tbody.appendChild(row);
                });
            })
            .catch(err => showMessage("error-box", err.message));
}

// Ação: Mapeia o registro selecionado de volta para o formulário
function selectRecord(code, desc, discount) {
    document.getElementById("prodCode").value = code;
    document.getElementById("prodCode").disabled = true; // Bloqueia a PK na edição
    document.getElementById("description").value = desc;
    document.getElementById("discountCode").value = discount === "None" ? "" : discount;

    document.getElementById("form-title").innerText = "Update Product Code: " + code;
    document.getElementById("btn-delete").classList.remove("hidden");
    isEditMode = true;
    hideMessages();
}

// Chamada Assíncrona: Envia o payload estruturado (Record DTO) via POST
function handleSave(event) {
    event.preventDefault();
    hideMessages();

    const payload = {
        prodCode: document.getElementById("prodCode").value.toUpperCase().trim(),
        description: document.getElementById("description").value.trim(),
        discountCode: document.getElementById("discountCode").value
    };

    fetch(API_URL, {
        method: "POST",
        headers: {"Authorization": AUTH_HEADER, "Content-Type": "application/json"},
        body: JSON.stringify(payload)
    })
            .then(res => {
                if (!res.ok)
                    throw new Error("Transaction rejected by Core business layer.");

                showMessage("success-box", "Product Code processed successfully.");
                clearForm();
                loadTableData();
            })
            .catch(err => showMessage("error-box", err.message));
}

// Chamada Assíncrona: Remove o registro de forma lógica ou física via DELETE
function handleDelete() {
    const code = document.getElementById("prodCode").value;
    if (!code || !confirm(`Drop product code "${code}" from system catalogs?`))
        return;

    hideMessages();

    fetch(`${API_URL}/${code}`, {
        method: "DELETE",
        headers: {"Authorization": AUTH_HEADER}
    })
            .then(res => {
                if (!res.ok)
                    throw new Error("Deletion rejected. Code may be bound to an active Product.");

                showMessage("success-box", "Product Code dropped completely.");
                clearForm();
                loadTableData();
            })
            .catch(err => showMessage("error-box", err.message));
}

// Utilitários de reset e interface
function clearForm() {
    document.getElementById("product-code-form").reset();
    document.getElementById("prodCode").disabled = false;
    document.getElementById("form-title").innerText = "New Product Code";
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