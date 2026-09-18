const BASE_URL = "/api";

async function handleResponse(response) {
  if (!response.ok) {
    throw new Error(`Request failed with status ${response.status}`);
  }
  return response.json();
}

export function getUser(userId) {
  return fetch(`${BASE_URL}/users/${userId}`).then(handleResponse);
}

export function createUser(name, email) {
  return fetch(`${BASE_URL}/users`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ name, email }),
  }).then(handleResponse);
}

export function createAccount(userId, accountType) {
  return fetch(`${BASE_URL}/accounts`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ userId: Number(userId), accountType }),
  }).then(handleResponse);
}

export function getAccountsByUser(userId) {
  return fetch(`${BASE_URL}/users/${userId}/accounts`).then(handleResponse);
}

export function depositToAccount(accountId, amount) {
  return fetch(`${BASE_URL}/accounts/${accountId}/deposit`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ amount: Number(amount) }),
  }).then(handleResponse);
}

export function withdrawFromAccount(accountId, amount) {
  return fetch(`${BASE_URL}/accounts/${accountId}/withdraw`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ amount: Number(amount) }),
  }).then(handleResponse);
}
