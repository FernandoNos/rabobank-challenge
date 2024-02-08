import { Component } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, FormGroup, Validators } from '@angular/forms';
import { MatIconModule} from '@angular/material/icon';
import { MatInputModule} from '@angular/material/input';
import { MatFormFieldModule} from '@angular/material/form-field';
import { CommonModule, CurrencyPipe } from '@angular/common';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatButtonModule} from '@angular/material/button';
import { MatCardModule} from '@angular/material/card';
import { EMICalculatorClient } from './emi-calculator-form.clients'

@Component({
  selector: 'app-emi-calculator-form',
  standalone: true,
  imports:  [
    MatFormFieldModule, 
    MatInputModule, 
    MatIconModule, 
    ReactiveFormsModule, 
    CommonModule, 
    CurrencyPipe,
    MatProgressSpinnerModule, 
    MatButtonModule,
    MatCardModule
  ],
  providers: [ CurrencyPipe],
  templateUrl: './emi-calculator-form.component.html',
  styleUrl: './emi-calculator-form.component.css'
})
export class EmiCalculatorFormComponent {
  loanForm!: FormGroup;
  emiResult: string = '';
  awaitingServerResponse: boolean = false;

  constructor(
    private emiCalculatorClient: EMICalculatorClient,
    private formBuilder: FormBuilder,
    private currencyPipe: CurrencyPipe
    ) { }
  ngOnInit() {
    this.createForm();
  }

  createForm() {
    this.loanForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      loanValue: ['', [Validators.required, Validators.min(0)]],
      yearlyInterest: ['', [Validators.required, Validators.min(0), Validators.max(100)]],
      loanTerms: ['', [Validators.required, Validators.min(0), Validators.max(30)]]
    });
  }

  async onSubmit() {  
    if (this.loanForm.valid) {
      this.awaitingServerResponse = true;
      const email = this.loanForm.value.email;
      const loanValue = this.loanForm.value.loanValue;
      const yearlyInterest = this.loanForm.value.yearlyInterest;
      const loanTerms = this.loanForm.value.loanTerms;
      
      try{
        var result = await this.emiCalculatorClient.requestLoan(email, loanValue, yearlyInterest, loanTerms)
      
        if(result != undefined){
          this.emiResult = "Total: "+this.currencyPipe.transform(result, '€')!;
        }
        else alert("There was an error calculating your EMI.")
      }catch(error){
        console.log(error)
        alert("There was an error calculating your EMI.")
      } finally {
        this.awaitingServerResponse = false;
      }
    }
  }

  shouldHideTotalLabel() {
    if(this.emiResult) {
      return false;
    }
    return true;
  }

  shouldHideSpinner(){
    return !this.awaitingServerResponse;
  }

  shouldHideForm(){
    return this.awaitingServerResponse;
  }
}