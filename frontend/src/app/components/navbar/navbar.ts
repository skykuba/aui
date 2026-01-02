import {Component} from '@angular/core';
import {RouterLink, RouterLinkActive} from '@angular/router';
import {NgOptimizedImage} from '@angular/common';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.html',
  imports: [
    RouterLink,
    RouterLinkActive,
    NgOptimizedImage
  ],
  styleUrl: './navbar.css'
})
export class Navbar {

}
