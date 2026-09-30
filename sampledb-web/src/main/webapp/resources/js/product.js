const API_URL = "/sampledb/api/products";
const MANUFACTURER_API_URL = "/sampledb/api/manufacturers";
const PRODUCT_CODE_API_URL = "/sampledb/api/product-codes";
const AUTH_HEADER = "Basic " + btoa("manager:123");

let isEditMode = false;

document.addEventListener("DOMContentLoaded", () => {
    // Inicialização coordenada da tela carregando as duas combos paralelas
    loadManufacturerCombo();
    loadProductCodeCombo();
    loadTableData();
    document.getElementById("product-form").addEventListener("submit", handleSave);
    document.getElementById("btn-clear").addEventListener("click", clearForm);
    document.getElementById("btn-delete").addEventListener("click", handleDelete);
});

// COMBO FILLER 1: Busca e popula a lista de Fabricantes
function loadManufacturerCombo() {
    fetch(MANUFACTURER_API_URL, {
        method: "GET",
        headers: {"Authorization": AUTH_HEADER, "Accept": "application/json"}
    })
            .then(res => res.json())
            .then(data => {
                const select = document.getElementById("manufacturerId");
                select.innerHTML = '<option value="">Select a Manufacturer</option>';
                data.forEach(item => {
                    const opt = document.createElement("option");
                    opt.value = item.manufacturerId;
                    opt.innerText = item.name + " (ID: " + item.manufacturerId + ")";
                    select.appendChild(opt);
                });
            })
            .catch(() => {
                document.getElementById("manufacturerId").innerHTML = '<option value="">Failed to sync manufacturers</option>';
            });
}

// COMBO FILLER 2: Busca e popula a lista de Códigos de Produto
function loadProductCodeCombo() {
    fetch(PRODUCT_CODE_API_URL, {
        method: "GET",
        headers: {"Authorization": AUTH_HEADER, "Accept": "application/json"}
    })
            .then(res => res.json())
            .then(data => {
                const select = document.getElementById("prodCode");
                select.innerHTML = '<option value="">Select a Product Code</option>';
                data.forEach(item => {
                    const opt = document.createElement("option");
                    opt.value = item.prodCode;
                    opt.innerText = item.prodCode + " - " + item.description;
                    select.appendChild(opt);
                });
            })
            .catch(() => {
                document.getElementById("prodCode").innerHTML = '<option value="">Failed to sync product codes</option>';
            });
}
// FETCH GET: Carrega o inventário e monta as linhas da tabela
function loadTableData() {
    fetch(API_URL, {
        method: "GET",
        headers: {"Authorization": AUTH_HEADER, "Accept": "application/json"}
    })
            .then(res => {
                if (res.status === 401 || res.status === 403)
                    throw new Error("Access Denied: Invalid privileges.");
                if (!res.ok)
                    throw new Error("Server error while reading database inventory records.");
                return res.json();
            })
            .then(data => {
                const tbody = document.getElementById("product-table-body");
                tbody.innerHTML = "";
                if (data.length === 0) {
                    tbody.innerHTML = '<tr><td colspan="7" class="p-4 text-center text-gray-400 italic">No products found.</td></tr>';
                    return;
                }

                data.forEach(item => {
                    const row = document.createElement("tr");
                    row.className = "hover:bg-gray-50 transition";
                    const code = item.productCode ? item.productCode.prodCode : "None";
                    const manufacturerName = item.manufacturer ? item.manufacturer.name : "None";
                    const manufacturerId = item.manufacturer ? item.manufacturer.manufacturerId : "";
                    const cost = item.purchaseCost ? parseFloat(item.purchaseCost).toFixed(2) : "0.00";

                    const statusStr = String(item.available).toUpperCase().trim();
                    const activeBadge = statusStr === "TRUE"
                            ? '<span class="bg-green-100 text-green-800 text-xs px-2 py-0.5 rounded-full font-bold">TRUE</span>'
                            : '<span class="bg-red-100 text-red-800 text-xs px-2 py-0.5 rounded-full font-bold">FALSE</span>';
                    const link = document.createElement("a");
                    link.href = "#";
                    link.className = "text-blue-600 hover:text-blue-800 font-bold hover:underline";
                    link.innerText = item.productId;
                    link.addEventListener("click", (event) => {
                        event.preventDefault();
                        selectRecord(item, code, manufacturerId);
                    });
                    // Concatenação clássica homologada para silenciar o validador do NetBeans
                    row.innerHTML = '<td></td>' +
                            '<td class="p-3 font-semibold text-gray-600">' + code + '</td>' +
                            '<td class="p-3 text-gray-700">' + item.description + '</td>' +
                            '<td class="p-3 text-gray-500">' + manufacturerName + '</td>' +
                            '<td class="p-3 text-right font-medium text-gray-800">$' + cost + '</td>' +
                            '<td class="p-3 text-right text-gray-600">' + item.quantityOnHand + '</td>' +
                            '<td class="p-3 text-center">' + activeBadge + '</td>';
                    row.querySelector("td").className = "p-3";
                    row.querySelector("td").appendChild(link);
                    tbody.appendChild(row);
                });
            })
            .catch(err => showMessage("error-box", err.message));
}

// Ação: Ao selecionar uma linha, reidrata o formulário e trava a PK
function selectRecord(item, code, manufacturerId) {
    document.getElementById("productId").value = item.productId;
    document.getElementById("productId").disabled = true;
    document.getElementById("prodCode").value = code;
    document.getElementById("manufacturerId").value = manufacturerId;
    document.getElementById("description").value = item.description || "";
    document.getElementById("available").value = item.available || "TRUE";
    document.getElementById("purchaseCost").value = item.purchaseCost || 0;
    document.getElementById("quantityOnHand").value = item.quantityOnHand || 0;
    document.getElementById("markup").value = item.markup || 0;
    document.getElementById("form-title").innerText = "Update Product: " + item.productId;
    document.getElementById("btn-delete").classList.remove("hidden");
    isEditMode = true;
    hideMessages();
}

// FETCH POST: Envia o payload estruturado (Record DTO) de volta para o Java
function handleSave(event) {
    event.preventDefault();
    hideMessages();

    const isAvailable = document.getElementById("available").value === "TRUE";
    const payload = {
        productId: parseInt(document.getElementById("productId").value),
        prodCode: document.getElementById("prodCode").value,
        manufacturerId: parseInt(document.getElementById("manufacturerId").value),
        description: document.getElementById("description").value.trim(),
        available: isAvailable,
        purchaseCost: parseFloat(document.getElementById("purchaseCost").value),
        quantityOnHand: parseInt(document.getElementById("quantityOnHand").value),
        markup: parseFloat(document.getElementById("markup").value)
    };
    fetch(API_URL, {
        method: "POST",
        headers: {"Authorization": AUTH_HEADER, "Content-Type": "application/json"},
        body: JSON.stringify(payload)
    })
            .then(res => {
                if (!res.ok) {
                    return res.text().then(textErr => {
                        throw new Error(textErr || "Transaction rejected.");
                    });
                }
                showMessage("success-box", "Product database synchronized successfully!");
                clearForm();
                loadTableData();
            })
            .catch(err => showMessage("error-box", err.message));
}

// FETCH DELETE: Remove o produto se ele não estiver preso a nenhuma ordem ativa
function handleDelete() {
    const id = document.getElementById("productId").value;
    if (!id || !confirm("Drop product ID '" + id + "' from enterprise catalogs?"))
        return;
    hideMessages();
    fetch(API_URL + "/" + id, {
        method: "DELETE",
        headers: {"Authorization": AUTH_HEADER}
    })
            .then(res => {
                if (!res.ok)
                    throw new Error("Drop operation rejected. Product is bound to a active Order.");
                showMessage("success-box", "Product completely removed from catalog boundaries.");
                clearForm();
                loadTableData();
            })
            .catch(err => showMessage("error-box", err.message));
}

// Utilitários de Interface e Reset
function clearForm() {
    document.getElementById("product-form").reset();
    document.getElementById("productId").disabled = false;
    document.getElementById("form-title").innerText = "New Product";
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
