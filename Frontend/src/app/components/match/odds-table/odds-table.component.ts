import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-odds-table',
  templateUrl: './odds-table.component.html',
  styleUrls: ['./odds-table.component.css']
})
export class OddsTableComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {
  }

  @Input() dataSource: { [key: string]: any }[] = [];
  @Input() colLabels: { [key: string]: any } = {};
  @Input() displayedColumns: string[] = [];

}
