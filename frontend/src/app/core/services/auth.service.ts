import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private isAuthenticatedSubject = new BehaviorSubject<boolean>(false);
  public isAuthenticated$: Observable<boolean> = this.isAuthenticatedSubject.asObservable();

  private usernameSubject = new BehaviorSubject<string | null>(null);
  public username$: Observable<string | null> = this.usernameSubject.asObservable();

  constructor(private router: Router) {}

  login(username: string): void {
    // In a real app, you'd call an API and handle the response
    this.isAuthenticatedSubject.next(true);
    this.usernameSubject.next(username);
    this.router.navigate(['/home']);
  }

  logout(): void {
    this.isAuthenticatedSubject.next(false);
    this.usernameSubject.next(null);
    this.router.navigate(['/login']);
  }

  public get currentUsername(): string | null {
    return this.usernameSubject.value;
  }

  public getIsAuthenticated(): boolean {
    return this.isAuthenticatedSubject.value;
  }
}
