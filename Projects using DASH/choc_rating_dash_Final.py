import dash
import dash_core_components as dcc
import dash_html_components as html
import plotly.graph_objects as go
import plotly.express as px
import pandas as pd


# For Data Cleaning and sorting the dataframe based on Rating
def DataCleaning(filename):
    df = pd.read_csv(filename)
    df.columns=df.columns.str.replace(r'\s+|\\n', ' ', regex=True)
    df.columns=df.columns.str.replace('Specific Bean Origin or ', '', regex=False)
    df.drop(df.loc[df['Broad Bean Origin']==' '].index, inplace=True)
    df.sort_values(by=['Rating'], inplace=True, ascending=False)
    return df

# External CSS Stylesheet
external_stylesheets = ['https://codepen.io/chriddyp/pen/bWLwgP.css']

app = dash.Dash(__name__, external_stylesheets=external_stylesheets)

# Calling DataCleaning Function for the data sheet
df = DataCleaning('flavors_of_cacao.csv')

# Created for displaying a list of Options in Drop Down box
country_list = df['Company Location'].dropna().unique()

# Dash layout
app.layout = html.Div([
# For the Main Title of Chart
    html.H2(children='World\'s Chocolate Bar Rating',style={
            'textAlign': 'center',
            'color': '#000080'
        }
    ),
    #  Drop Down Boxes and its styles
    html.Div([
        html.Div([
            dcc.Dropdown(
                id='country_explored',
                options=[{'label': i, 'value': i} for i in country_list],
                placeholder="Select a Country"
            )],style={'width': '20%','display': 'inline-block', 'padding': '20 20'}
        ),
        html.Div([
            dcc.Dropdown(id='company_list', placeholder="Select a Company")
        ],style={'width': '20%', 'display': 'inline-block','padding': '20 20'}
        ),
    ],style={'display':'flex', 'justify-content':'center'}
    ),
    # Chart and its Style
    html.Div([

        html.Div([
        # For Bar Chart
            html.Div([
            html.H5(children='Ratings of Bars produced',style={
                'textAlign': 'center',
                'color': '#000080'
            }),
            html.Div([
                dcc.Graph(id='crossfilter-bar')
            ])
            ],style={'display':'flex','flex-direction':'column','flex-grow':'1', 'margin':'5vh 2vh'}
        ),
        # For World Map Chart
        html.Div([
            html.H5(children='Cocoa Bean Origins',style={
                    'textAlign': 'center',
                    'color': '#000080'
                }),
            html.Div([
                dcc.Graph(id='crossfilter-source-country')
            ]),
            ],style={'display':'flex', 'flex-direction':'column', 'flex-grow':'1','margin':'5vh 2vh'}
        ),
        ],style={'display':'flex', 'justify-content':'space-around', 'margin':'5vh 5vh'}
    )
    ])
])


# Used for updating the bar chart based on Drop Down Selection
@app.callback(
    dash.dependencies.Output('crossfilter-bar', 'figure'),
    [dash.dependencies.Input('country_explored', 'value'),
    dash.dependencies.Input('company_list', 'value')])
def update_bar_graph(current_country,chosen_company):
    dff = df[df['Company Location'] == current_country][df['Company (Maker-if known)'] == chosen_company]
    return px.bar(dff, x='Bar Name', y='Rating' )

# Used for updating the Company Drop Down box based on Country Drop Down Selection
@app.callback(
    dash.dependencies.Output('company_list', 'options'),
    [dash.dependencies.Input('country_explored', 'value')])
def update_company_list(current_country):
    dff = df[df['Company Location'] == current_country]
    return [{'label': i, 'value': i} for i in dff['Company (Maker-if known)'].unique()]


# Used for updating the world map chart based on Drop Down Selection
@app.callback(
    dash.dependencies.Output('crossfilter-source-country', 'figure'),
    [dash.dependencies.Input('country_explored', 'value'),
    dash.dependencies.Input('company_list', 'value')])
def update_map(current_country, chosen_company):

    dff = df[df['Company Location'] == current_country][df['Company (Maker-if known)'] == chosen_company]

    mapFig = go.Figure(data=go.Choropleth(
        locations = dff['Broad Bean Origin'].dropna(),
        locationmode = 'country names',
        z = dff['Rating'],
        text = 'Bar Name: '+dff['Bar Name'],
        colorscale = 'YlGnBu',
        marker_line_color='darkgray',
        marker_line_width=0.5,
        colorbar_tickprefix = '',
        colorbar_title = 'Rating',
    ))

    mapFig.update_layout(
        title_text='Sources of Cocoa for ' + str(chosen_company) + '<br/>',
        geo=dict(
            showframe=False,
            showcoastlines=False,
            projection_type='equirectangular'
        ),margin={"r":0,"t":0,"l":0,"b":0}
    )

    return mapFig


# Main Function
if __name__ == '__main__':
    app.run_server(debug=True)
