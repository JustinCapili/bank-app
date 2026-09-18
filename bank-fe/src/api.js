const BASE_URL = "/api";
const TOKEN_KEY = "authToken";

export function getToken() {
  return localStorage.getItem(TOKEN_KEY);
}

export function setToken(token) {
  localStorage.setItem(TOKEN_KEY, token);
}

export function clearToken() {
  localStorage.removeItem(TOKEN_KEY);
}

function authHeaders() {
  const token = getToken();
  return token ? { Authorization: `Bearer ${token}` } : {};
}

async function handleResponse(response) {
  if (!response.ok) {
    if (response.status === 401 || response.status === 403) {
      clearToken();
    }
    throw new Error(`Request failed with status ${response.status}`);
  }
  return response.json();
}

export function getUser(userId) {
  return fetch(`${BASE_URL}/users/${userId}`, {
    headers: { ...authHeaders() },
  }).then(handleResponse);
}

export function login(userId, password) {
  return fetch(`${BASE_URL}/auth/login`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ userId: Number(userId), password }),
  }).then(handleResponse);
}

export function createUser(name, email, password) {
  return fetch(`${BASE_URL}/users`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ name, email, password }),
  }).then(handleResponse);
}

export function createAccount(userId, accountType) {
  return fetch(`${BASE_URL}/accounts`, {
    method: "POST",
    headers: { "Content-Type": "application/json", ...authHeaders() },
    body: JSON.stringify({ userId: Number(userId), accountType }),
  }).then(handleResponse);
}

export function getAccountsByUser(userId) {
  return fetch(`${BASE_URL}/users/${userId}/accounts`, {
    headers: { ...authHeaders() },
  }).then(handleResponse);
}

export function depositToAccount(accountId, amount) {
  return fetch(`${BASE_URL}/accounts/${accountId}/deposit`, {
    method: "PUT",
    headers: { "Content-Type": "application/json", ...authHeaders() },
    body: JSON.stringify({ amount: Number(amount) }),
  }).then(handleResponse);
}

export function withdrawFromAccount(accountId, amount) {
  return fetch(`${BASE_URL}/accounts/${accountId}/withdraw`, {
    method: "PUT",
    headers: { "Content-Type": "application/json", ...authHeaders() },
    body: JSON.stringify({ amount: Number(amount) }),
  }).then(handleResponse);
}
