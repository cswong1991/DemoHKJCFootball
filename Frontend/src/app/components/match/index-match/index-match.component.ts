import { HttpClient } from '@angular/common/http';
import { Component, OnInit, ViewChild } from '@angular/core';
import { MatAccordion } from '@angular/material/expansion';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-index-match',
  templateUrl: './index-match.component.html',
  styleUrls: ['./index-match.component.css']
})
export class IndexMatchComponent implements OnInit {

  constructor(
    private http: HttpClient
  ) { }

  ngOnInit(): void {
    this.getIndex();
  }

  @ViewChild(MatAccordion) accordion!: MatAccordion;
  public finishInit: boolean = false;
  public matchIndex: { [key: string]: any }[] = [];
  public matchDate: string[] = [];

  public getIndex(): void {
    this.http.get<{ [key: string]: any }[]>(environment.apiUrl + "/active-match/", {}).subscribe(res => {
      this.matchIndex = res;
      this.matchDate = [...new Set(res.map((e1: { [key: string]: any }) => e1['matchDate'].split('+')[0]))];
      this.finishInit = true;
    });
  }

}
