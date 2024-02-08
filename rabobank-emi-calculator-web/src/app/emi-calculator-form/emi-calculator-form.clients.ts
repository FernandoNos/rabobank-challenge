import { Injectable } from '@angular/core';

@Injectable({
    providedIn: 'root'
  })
export class EMICalculatorClient {

    constructor() { }

    async requestLoan(email: string, loanValue: number, yearlyInterest: number, loanTerms: number) {
        var data = {
          "email" : email,
          "loan_value": loanValue,
          "yearly_interest_rate": yearlyInterest,
          "loan_term_in_years": loanTerms,
        }
    
        return fetch('http://localhost:8080/loans/emi/estimates', {
          method: 'POST',
          body : JSON.stringify(data),
          headers : {
            "Content-Type": "application/json",
        }
        })
        .then(res => res.json())
        .then(res => {
          return res.value;
        });
      }
}

