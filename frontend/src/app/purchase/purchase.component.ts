import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../service/api.service';

@Component({
  selector: 'app-purchase',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './purchase.component.html',
  styleUrl: './purchase.component.css'
})
export class PurchaseComponent implements OnInit {
  constructor(private apiService: ApiService){}

  products: any[] = []
  suppliers: any[] = []
  productId:string = ''
  supplierId:string = ''
  description:string = ''
  quantity:string = ''
  message:string = ''
  loading:boolean = true
  

  ngOnInit(): void {
    this.fetchProductsAndSuppliers();
  }

  fetchProductsAndSuppliers():void{
    
   
      this.apiService.getAllProducts().subscribe({
      next: (res: any) => {
        if (res.status === 200 && res.products) {
          this.products = res.products;
        }else{
          this.showMessage("Failed to fetch product")
        }
      },
      error: (error) => {
        this.showMessage(
          error?.error?.message ||
            error?.message ||
            'Unable to get Products' + error
        );
        
      },
    });

    this.apiService.getAllSuppliers().subscribe({
      next: (res: any) => {
        if (res.status === 200 && res.suppliers) {
          this.suppliers = res.suppliers;
        }else{
          this.showMessage("Failed to fetch product");
        }
      },
      error: (error) => {
        this.showMessage(
          error?.error?.message ||
            error?.message ||
            'Unable to get suppliers' + error
        );
      },
    })
  }

  //Handle form submission
  handleSubmit():void{
    if (!this.productId || !this.supplierId || !this.quantity) {
      this.showMessage("Please fill all fields")
      return;
    }
    const body = {
      productId: this.productId,
      supplierId: this.supplierId,
      quantity:  parseInt(this.quantity, 10),
      description: this.description
    }

    this.apiService.purchaseProduct(body).subscribe({
      next: (res: any) => {
        if (res.status === 200) {
          this.showMessage(res.message)
          this.resetForm();
        }
      },
      error: (error) => {
        this.showMessage(
          error?.error?.message ||
            error?.message ||
            'Unable to get purchase a product' + error
        );
      },
    })

  }

  
  resetForm():void{
    this.productId = '';
    this.supplierId = '';
    this.description = '';
    this.quantity = '';
  }


  



  showMessage(message: string) {
    this.message = message;
    setTimeout(() => {
      this.message = '';
    }, 4000);
  }
}
