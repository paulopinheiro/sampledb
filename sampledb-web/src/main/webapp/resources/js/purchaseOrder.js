const API_URL = "/sampledb/api/purchase-orders";
const CUSTOMER_API_URL = "/sampledb/api/customers";
const PRODUCT_API_URL = "/sampledb/api/products";

// Coleta a credencial configurada na index de forma dinâmica
const AUTH_HEADER = sessionStorage.getItem("active_auth") || ("Basic " + btoa("manager:123"));

let isEditMode = false;

document.addEventListener("DOMContentLoaded", () => {
    // Orquestração paralela de carga do balcão
    loadCustomerCombo();
    loadProductCombo();
    loadTableData();

    document.getElementById("order-form").addEventListener("submit", handleSave);
    document.getElementById("btn-clear").addEventListener("click", clearForm);
    document.getElementById("btn-delete").addEventListener("click", handleDelete);
});

// COMBO FILLER 1: Busca e popula a lista de Clientes
function loadCustomerCombo() {
    fetch(CUSTOMER_API_URL, {
        method: "GET",
        headers: {"Authorization": AUTH_HEADER, "Accept": "application/json"}
    })
            .then(res => res.json())
            .then(data => {
                const select = document.getElementById("customerId");
                select.innerHTML = '<option value="">Select a Customer</option>';
                data.forEach(item => {
                    const opt = document.createElement("option");
                    opt.value = item.customerId;
                    opt.innerText = item.name + " (ID: " + item.customerId + ")";
                    select.appendChild(opt);
                });
            })
            .catch(() => {
                document.getElementById("customerId").innerHTML = '<option value="">Failed to sync customers</option>';
            });
}

// COMBO FILLER 2: Busca e popula a lista de Catálogo de Produtos
function loadProductCombo() {
    fetch(PRODUCT_API_URL, {
        method: "GET",
        headers: {"Authorization": AUTH_HEADER, "Accept": "application/json"}
    })
            .then(res => res.json())
            .then(data => {
                const select = document.getElementById("productId");
                select.innerHTML = '<option value="">Select Merchandise</option>';
                data.forEach(item => {
                    const opt = document.createElement("option");
                    opt.value = item.productId;
                    opt.innerText = item.description + " (Stock: " + item.quantityOnHand + ")";
                    select.appendChild(opt);
                });
            })
            .catch(() => {
                document.getElementById("productId").innerHTML = '<option value="">Failed to sync inventory</option>';
            });
}

// FETCH GET: Carrega todos os pedidos e monta a tabela de auditoria
function loadTableData() {
    fetch(API_URL, {
        method: "GET",
        headers: {"Authorization": AUTH_HEADER, "Accept": "application/json"}
    })
            .then(res => {
                if (res.status === 401 || res.status === 403)
                    throw new Error("Access Denied: Privilégios insuficientes.");
                if (!res.ok)
                    throw new Error("Erro no servidor ao ler livros de registros de pedidos.");
                return res.json();
            })
            .then(data => {
                const tbody = document.getElementById("order-table-body");
                tbody.innerHTML = "";

                if (data.length === 0) {
                    tbody.innerHTML = '<tr><td colspan="6" class="p-4 text-center text-gray-400 italic">Nenhum pedido de compra emitido.</td></tr>';
                    return;
                }

                data.forEach(item => {
                    const row = document.createElement("tr");
                    row.className = "hover:bg-gray-50 transition";

                    // Navegação segura nas propriedades das Entidades Relacionais associadas
                    const customerName = item.customerId ? item.customerId.name : "N/A";
                    const customerId = item.customerId ? item.customerId.customerId : "";
                    const productDesc = item.productId ? item.productId.description : "N/A";
                    const productId = item.productId ? item.productId.productId : "";

                    const freight = item.shippingCost ? parseFloat(item.shippingCost).toFixed(2) : "0.00";

                    // Formatação amigável de data (Corta o timestamp do JPA ISO)
                    const dateRaw = item.salesDate ? item.salesDate.split("T")[0] : "N/A";

                    // Criação do link seguro em memória pura do Javascript
                    const link = document.createElement("a");
                    link.href = "#";
                    link.className = "text-blue-600 hover:text-blue-800 font-bold hover:underline";
                    link.innerText = item.orderNum;
                    link.addEventListener("click", (event) => {
                        event.preventDefault();
                        selectRecord(item, customerId, productId);
                    });

                    // Concatenação de string clássica imune a falsos positivos do NetBeans
                    row.innerHTML = '<td></td>' +
                            '<td class="p-3 text-gray-700 font-medium">' + customerName + '</td>' +
                            '<td class="p-3 text-gray-600">' + productDesc + '</td>' +
                            '<td class="p-3 text-right text-gray-800 font-bold">' + item.quantity + '</td>' +
                            '<td class="p-3 text-right text-gray-500">$' + freight + '</td>' +
                            '<td class="p-3 text-center text-gray-600">' + dateRaw + '</td>';

                    // Injeta o link operacional na primeira coluna da linha
                    row.querySelector("td").className = "p-3";
                    row.querySelector("td").appendChild(link);

                    tbody.appendChild(row);
                });
            })
            .catch(err => showMessage("error-box", err.message));
}

// Ação: Ao clicar no link, reidrata o formulário e congela o ID (PK)
function selectRecord(item, customerId, productId) {
    document.getElementById("orderNum").value = item.orderNum;
    document.getElementById("orderNum").disabled = true; // Chave primária imutável no update

    document.getElementById("customerId").value = customerId;
    document.getElementById("productId").value = productId;
    document.getElementById("quantity").value = item.quantity || 1;
    document.getElementById("shippingCost").value = item.shippingCost || 0;
    document.getElementById("shippingCompany").value = item.shippingCompany || "";

    // Tratamento de datas para injeção correta nos inputs do tipo date (formato YYYY-MM-DD)
    if (item.salesDate)
        document.getElementById("salesDate").value = item.salesDate.split("T")[0];
    if (item.shippingDate)
        document.getElementById("shippingDate").value = item.shippingDate.split("T")[0];

    document.getElementById("form-title").innerText = "Update Purchase Order: #" + item.orderNum;
    document.getElementById("btn-delete").classList.remove("hidden");
    isEditMode = true;
    hideMessages();
}

// FETCH POST: Envia os dados consolidados do DTO para a retaguarda do Java
function handleSave(event) {
    event.preventDefault();
    hideMessages();

    // Resgata os valores dos inputs de data de forma crua
    const salesRaw = document.getElementById("salesDate").value;
    const shippingRaw = document.getElementById("shippingDate").value;

    // Tratamento Seguro: Se o usuário preencheu, formata em ISO completo. Se não, manda null.
    const formattedSalesDate = salesRaw ? (salesRaw + "T00:00:00Z") : null;
    const formattedShippingDate = shippingRaw ? (shippingRaw + "T00:00:00Z") : null;

    const payload = {
        orderNum: parseInt(document.getElementById("orderNum").value),
        customerId: parseInt(document.getElementById("customerId").value),
        productId: parseInt(document.getElementById("productId").value),
        quantity: parseInt(document.getElementById("quantity").value),
        shippingCost: parseFloat(document.getElementById("shippingCost").value),
        shippingCompany: document.getElementById("shippingCompany").value.trim(),
        salesDate: formattedSalesDate, // Injeta a string tratada sem risco de 'undefined'
        shippingDate: formattedShippingDate // Injeta a string tratada sem risco de 'undefined'
    };

    fetch(API_URL, {
        method: "POST",
        headers: {"Authorization": AUTH_HEADER, "Content-Type": "application/json"},
        body: JSON.stringify(payload)
    })
            .then(res => {
                if (!res.ok) {
                    return res.text().then(textErr => {
                        throw new Error(textErr || "Transação financeira rejeitada.");
                    });
                }
                showMessage("success-box", "Ordem de compra faturada e integrada com sucesso.");
                clearForm();
                loadTableData();
                loadProductCombo();
            })
            .catch(err => showMessage("error-box", err.message));
}


// FETCH DELETE: Cancela e dropa a ordem física (Operação exclusiva Admin-Only)
function handleDelete() {
    const num = document.getElementById("orderNum").value;
    if (!num || !confirm("Deseja estornar permanentemente o pedido de compra #" + num + "?"))
        return;

    hideMessages();

    fetch(API_URL + "/" + num, {
        method: "DELETE",
        headers: {"Authorization": AUTH_HEADER}
    })
            .then(res => {
                if (res.status === 403)
                    throw new Error("Acesso Proibido: Operadores não possuem privilégios para cancelar ordens.");
                if (!res.ok)
                    throw new Error("Ocorreu uma falha ao tentar remover o registro do livro de transações.");
                showMessage("success-box", "Pedido de compra cancelado e removido do sistema.");
                clearForm();
                loadTableData();
                loadProductCombo();
            })
            .catch(err => showMessage("error-box", err.message));
}

// Utilitários de Interface e Limpeza
function clearForm() {
    document.getElementById("order-form").reset();
    document.getElementById("orderNum").disabled = false;
    document.getElementById("form-title").innerText = "New Purchase Order";
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

