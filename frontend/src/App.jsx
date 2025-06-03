import React, { useEffect, useState } from "react";
import axios from "axios";
import "./App.css";

const API_URL = "http://localhost:8080/api/crime";

function App() {
  const [crime, setCrime] = useState(null);
  const [selectedSuspect, setSelectedSuspect] = useState(null);
  const [questionIndex, setQuestionIndex] = useState(null);
  const [verdict, setVerdict] = useState(null);

  useEffect(() => {
    axios.get(API_URL).then((res) => {
      setCrime(res.data);
    });
  }, []);

  if (!crime) return <h2>Chargement de l'enquête...</h2>;

  const accuser = () => {
    if (!selectedSuspect) return;
    const coupable = crime.coupable.nom.toLowerCase();
    const nomChoisi = selectedSuspect.nom.toLowerCase();
    const isCorrect = nomChoisi === coupable;
    setVerdict(isCorrect ? "✅ Bonne réponse ! C'est bien le coupable." : `❌ Mauvaise réponse. Le coupable était ${crime.coupable.nom}`);
  };

  return (
    <div style={{ padding: "2rem", fontFamily: "Arial" }}>
      <h1>🔍 Meurtre de {crime.victime}</h1>

      <h2>🧩 Indices</h2>
      <ul>
        {crime.indices.map((i, index) => (
          <li key={index}>
            <strong>{i.nom}</strong>: {i.description}{" "}
            {i.crucial ? "(Preuve clé)" : ""}
          </li>
        ))}
      </ul>

      <h2>🧑‍⚖️ Suspects</h2>
      <ul>
        {crime.suspects.map((s, index) => (
          <li key={index}>
            <button
              style={{ marginTop: "0.5rem" }}
              onClick={() => {
                setSelectedSuspect(s);
                setQuestionIndex(null);
                setVerdict(null);
              }}
            >
              {s.nom}
            </button>
          </li>
        ))}
      </ul>

      {selectedSuspect && (
        <div style={{ marginTop: "2rem" }}>
          <h3>🕵️ Interrogatoire : {selectedSuspect.nom}</h3>
          <p>Alibi : {selectedSuspect.alibi}</p>
          <ul>
            {selectedSuspect.questions.map((q, i) => (
              <li key={i}>
                <button onClick={() => setQuestionIndex(i)}>
                  {q.question}
                </button>
              </li>
            ))}
          </ul>

          {questionIndex !== null && (
            <div style={{ marginTop: "1rem" }}>
              <p>
                <strong>Q:</strong>{" "}
                {selectedSuspect.questions[questionIndex].question}
              </p>
              <p>
                <strong>R:</strong>{" "}
                {selectedSuspect.questions[questionIndex].reponse}
              </p>
            </div>
          )}

          <button
            style={{ marginTop: "1rem", background: "#222", color: "#fff", padding: "0.5rem 1rem", border: "none", borderRadius: "5px" }}
            onClick={accuser}
          >
            ⚖️ Accuser ce suspect
          </button>

          {verdict && (
            <p style={{ marginTop: "1rem", fontWeight: "bold", fontSize: "1.2rem" }}>{verdict}</p>
          )}
        </div>
      )}
    </div>
  );
}

export default App;
 