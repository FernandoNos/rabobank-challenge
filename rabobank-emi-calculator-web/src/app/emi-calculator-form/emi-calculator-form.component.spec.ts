import { ComponentFixture, TestBed } from '@angular/core/testing';
import { EmiCalculatorFormComponent } from './emi-calculator-form.component';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { CommonModule, CurrencyPipe } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { provideAnimations } from '@angular/platform-browser/animations';
import { provideRouter, Routes } from '@angular/router';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { EMICalculatorClient } from './emi-calculator-form.clients';

const routes: Routes = [];

describe('EmiCalculatorFormComponent', () => {
  let component: EmiCalculatorFormComponent;
  let fixture: ComponentFixture<EmiCalculatorFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [],
      imports: [
        MatFormFieldModule, 
        MatInputModule, 
        MatIconModule, 
        ReactiveFormsModule, 
        CommonModule, 
        CurrencyPipe,
        MatProgressSpinnerModule, 
        MatButtonModule,
        MatCardModule,
        EmiCalculatorFormComponent,
        HttpClientTestingModule
      ],
      providers: [FormBuilder, CurrencyPipe, provideAnimations(), provideRouter(routes),
        {
          provide: EMICalculatorClient,
          useClass: MockEMI
       }]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EmiCalculatorFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize form on ngOnInit', () => {
    component.ngOnInit();
    expect(component.loanForm).toBeDefined();
  });

  it('should display total when emiResult is set', () => {
    component.emiResult = '1000';
    expect(component.shouldHideTotalLabel()).toBeFalsy();
  });

  it('should not display total when emiResult is not set', () => {
    component.emiResult = '';
    expect(component.shouldHideTotalLabel()).toBeTruthy();
  });

  it('should set emiResult if onSubmit successful', async () => {
    component.loanForm.controls['email'].setValue("test@test.com")
    component.loanForm.controls['loanValue'].setValue(1.0)
    component.loanForm.controls['yearlyInterest'].setValue(1.0)
    component.loanForm.controls['loanTerms'].setValue(1.0)
    
    expect(component.loanForm.valid).toBeTruthy()

    await component.onSubmit()
    expect(component.emiResult).toEqual("Total: €1.00");
    expect(component.shouldHideTotalLabel()).toBeFalsy();
    expect(component.awaitingServerResponse).toBeFalsy();
  });
  class MockEMI extends EMICalculatorClient {

    override async requestLoan(email: string, loanValue: number, yearlyInterest: number, loanTerms: number) {
      return Promise.resolve(1);
    }
  }
});
